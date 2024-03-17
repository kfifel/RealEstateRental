import {Component, OnInit} from '@angular/core';
import {BehaviorSubject, Observable, Subject} from "rxjs";
import {HttpHeaders, HttpResponse} from "@angular/common/http";
import {UserProfileService} from "../../core/services/user.service";
import {User} from "../../core/models/auth.models";
import {SweetAlertService} from "../../core/services/sweet-alert.service";
import {Role} from "../../core/models/role.enum";

@Component({
    selector: 'app-user-management',
    templateUrl: './user-management.component.html',
    styleUrls: ['./user-management.component.scss']
})
export class UserManagementComponent implements OnInit {


    users: User[] = [];
    // bread crumb items
    breadCrumbItems: Array<{}>;

    currentSearch: string;
    private searchSubject: Subject<string> = new Subject<string>();
    isLoading = false;
    allRoles = Object.values(Role);
    selectedRoles: { [key: number]: Role[] } = {};

    totalItems = 0;
    itemsPerPage = 5;
    page: number = 1;

    constructor(private userService: UserProfileService,
                private _alert: SweetAlertService) {
    }

    ngOnInit(): void {
        this.breadCrumbItems = [{label: 'Users', link: '/admin/users-management'}, {label: 'Users List', active: true}];

        this.loadAll();

    }

    loadAll(page?: number) {
        const pageToLoad: number = page ?? this.page ?? 1;
        this.isLoading = true;
        let requestObservable$: Observable<any>;
        if (this.currentSearch) {
            requestObservable$ = this.userService
                .search({
                    page: pageToLoad - 1,
                    query: this.currentSearch,
                    size: this.itemsPerPage,
                    sort: ['id,desc'],
                });
        } else {
            requestObservable$ = this.userService
                .query({
                    page: pageToLoad - 1,
                    size: this.itemsPerPage,
                    sort: ['id,desc'],
                })
        }

        requestObservable$
            .subscribe({
                next: (res: HttpResponse<User[]>) => {
                    this.isLoading = false;
                    this.onSuccess(res.body, res.headers, pageToLoad);
                },
                error: () => {
                    this.isLoading = false;
                    this.onError();
                },
            });
    }

    handleSearch() {
        this.loadAll(1);
    }

    // Call this method when the search input changes
    onSearchChange(): void {
        this.searchSubject.next(this.currentSearch);
    }

    private onSuccess(body: User[], headers: HttpHeaders, pageToLoad: number) {
        this.isLoading = false;
        this.totalItems = Number(headers.get('X-Total-Count'));
        this.page = pageToLoad;
        this.users = body;
        this.selectedRoles = body.map(
            (user) => ({[user.id]: user.authorities}))
            .reduce((acc, x) => ({...acc, ...x}), {});
    }

    private onError() {
        console.error("Error loading properties.")
    }

    alert(enable: boolean, id: number) {
        let user = this.findMemberById(id);
        this._alert.confirm(
            "Enable/disable members",
            `Do you want to  ${enable ? 'enabled' : 'disabled'} User : ${user.firstName} ${user.lastName}`,
            () => {
                this.enableMember(user, enable);
            },
            () => {
                user.enabled = !enable;
            }
        );
    }


    enableMember(member: User, enable: boolean) {
        this.userService.enableMember(member.id, enable).subscribe(
            () => {
                this._alert.success('Success', `User has been ${enable ? 'enabled' : 'disabled'} successfully`);
            },
            (error) => {
                member.enabled = !enable;
                console.error(error);
                this._alert.error('Error', 'An error occurred while enabling/disabling member');
            }
        );
    }

    updateRoles(userId: number) {
        this._alert.isConfirm(
            'Update roles',
            'Do you want to update roles for this user?'
        ).then((isConfirm) => {
            const roles = this.selectedRoles[userId];
            const user = this.findMemberById(userId);

            if (!isConfirm) {
                this.selectedRoles[userId] = user.authorities;
                return;
            }

            if (user.id) {
                this.userService.updateRoles(userId, roles).subscribe(
                    () => {
                        user.authorities = roles;
                        this._alert.success('Success', `Roles for user ${user.email} has been updated successfully`);
                    },
                    (error) => {
                        this._alert.error('Error', error.error.errors[0].message ?? 'An error occurred while updating roles');
                    }
                );
            }
        });

    }

    findMemberById(id: number) {
        return this.users.find(user => user.id === id);
    }
}
