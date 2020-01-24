import { ManifestationSection } from './manifestation-section.model';
import { ManifestationImage } from './manifestation-image-model';

export class Manifestation {

    manifestationId: number;
    name: string;
    description: string;
    type: string;
    maxReservations: number;
    manifestationDates: Array<Date>;
    reservableUntil: Date;
    reservationsAllowed: boolean;
    images: Array<ManifestationImage>;
    selectedSections: Array<ManifestationSection>;
    locationId: number;
    locationName: string;

    constructor() {
        this.reservationsAllowed = false;
    }
}