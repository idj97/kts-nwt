import { ManifestationSection } from './manifestation-section.model';

export class Manifestation {

    manifestationId: number;
    name: string;
    description: string;
    type: string;
    maxReservations: number;
    manifestationDates: Array<Date>;
    reservableUntil: Date;
    reservationsAllowed: boolean;
    images: Array<string>;
    selectedSections: Array<ManifestationSection>;
    locationId: number;

    constructor() {

    }
}