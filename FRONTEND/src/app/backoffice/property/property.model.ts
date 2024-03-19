import {User} from "../../core/models/auth.models";
import {RentStatus} from "../../core/models/rent.model";

export interface IProperty {

  id?: number; // Optional as it might not be available until persisted
  title: string;
  description: string;
  size: number;
  pricePerMonth: number;
  pricePerDay: number;
  numberOfRooms: number;
  hasBalcony: boolean;
  propertyType: PropertyType;
  landlord?: User; // Optional as it might not be available until associated
  address: string;
  city?: string; // Optional as it might not be available until associated
  images?: Image[];
}

export class PropertyModel implements IProperty {
  id?: number;
  title: string;
  description: string;
  size: number;
  pricePerMonth: number;
  pricePerDay: number;
  numberOfRooms: number;
  hasBalcony: boolean;
  propertyType: PropertyType;
  landlord?: User;
  address: string;
  city?: string;
  images?: Image[];

  constructor(data: Partial<IProperty> = {}) {
    Object.assign(this, data);
  }
}

export interface PropertySearch {

  title: string;
  description: string;
   address: string;
  numberOfRooms: number;
  minPricePerDay: number;
  maxPricePerDay: number;
  propertyType: PropertyType;
  city: string;
}



export enum PropertyType {

  APARTMENT= 'APARTMENT',
  HOUSE = 'HOUSE',
  VILLA = 'VILLA',
  STUDIO = 'STUDIO',
  LOFT = 'LOFT',
  DUPLEX = 'DUPLEX',
  PENTHOUSE = 'PENTHOUSE',
  TRIPLEX = 'TRIPLEX',
  TOWNHOUSE = 'TOWNHOUSE',
  CONDO = 'CONDO',
  ROOM = 'ROOM',
  OTHER = 'OTHER'
}

export interface Image {

  id: number;
  base64: string;
}

export interface IRent {
  id: number;
  startDate: string;
  endDate: string;
  totalPrice: number;
  isPaid: boolean;
  status: RentStatus;
  ownerFullName: string;
  ownerPhone: string;
  tenantFullName: string;
}
