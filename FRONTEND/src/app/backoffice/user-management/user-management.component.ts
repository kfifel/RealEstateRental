import { Component, OnInit } from '@angular/core';
import {BehaviorSubject, Observable, Subject} from "rxjs";
import {HttpHeaders, HttpResponse} from "@angular/common/http";
import {UserProfileService} from "../../core/services/user.service";
import {User} from "../../core/models/auth.models";
import {SweetAlertService} from "../../core/services/sweet-alert.service";

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

  totalItems = 0;
  itemsPerPage = 5;
  page: number = 1;

  constructor(private userService: UserProfileService,
              private _alert: SweetAlertService) { }

  ngOnInit(): void {
    this.breadCrumbItems = [{ label: 'Users', link: '/admin/users-management' }, { label: 'Users List', active: true }];

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
  }

  private onError() {
    console.error("Error loading properties.")
  }

    alert(enable: boolean, id: number) {
        let user = this.findMemberById(id);
        this._alert.confirm(
            "Enable/disable members",
            `Do you want to  ${enable ? 'enabled' : 'disabled'} User : ${user.firstName} ${user.lastName}`,
            () => {this.enableMember(user, enable);},
            () => {user.enabled = !enable;}
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

    findMemberById(id: number) {
        return this.users.find(member => member.id === id);
    }
}
