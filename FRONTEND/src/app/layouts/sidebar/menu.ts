import {MenuItem} from './menu.model';
import {Role} from "../../core/models/role.enum";

export const MENU: MenuItem[] = [
    {
        id: 1,
        label: 'MENUITEMS.MENU.TEXT',
        isTitle: true
    },
    {
        id: 2,
        label: 'MENUITEMS.DASHBOARDS.TEXT',
        icon: 'bx-home-circle',
        link: '/admin/dashboard',
        badge: {
            variant: 'info',
            text: 'MENUITEMS.DASHBOARDS.BADGE',
        }
    },
    {
        id: 7,
        isLayout: true
    },
    {
        id: 8,
        label: 'MENUITEMS.APPS.TEXT',
        isTitle: true
    },
    {
        id: 9,
        label: 'MENUITEMS.PROPERTY.TEXT',
        icon: 'bx-calendar',
        link: '/admin/property',
        roles: [Role.ROLE_PROPERTY]
    },
    {
        id: 9,
        label: 'MENUITEMS.USERS_MANAGEMENT.TEXT',
        icon: 'bxs-user',
        link: '/admin/users-management',
        roles: [Role.ROLE_ADMIN]
    },
];

