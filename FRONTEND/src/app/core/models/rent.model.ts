import {IProperty} from "../../backoffice/property/property.model";

export interface IRent {
    id: number;
    startDate: string; // Assuming startDate and endDate are in ISO 8601 format (e.g., '2024-03-15')
    endDate: string;
    totalPrice: number;
    isPaid: boolean;
    status: RentStatus;
    property: IProperty;
    ownerFullName: string;
    ownerPhone: string;
    tenantFullName: string;
}

export enum RentStatus {

  PENDING = 'PENDING',
  ONGOING = 'ONGOING',
  FINISHED = 'FINISHED',
  CANCELLED = 'CANCELLED',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED'
}
