import { ValidatorFn, FormGroup, ValidationErrors } from '@angular/forms';


export const maxReservationsValidator: ValidatorFn = (control: FormGroup): ValidationErrors | null => {
    const reservationsAllowed = control.get('reservationsAllowed');
    const maxReservations = control.get('maxReservations');
  
    if(reservationsAllowed.value && maxReservations.value == null) {
        return { 'maxReservationsEmpty': true };
    }
    return null;
};

export const reservableUntilValidator: ValidatorFn = (control: FormGroup): ValidationErrors | null => {
    const reservationsAllowed = control.get('reservationsAllowed');
    const reservableUntil = control.get('reservableUntil');
  
    if(reservationsAllowed.value && reservableUntil.value == null) {
        return { 'reservableUntilEmpty': true };
    }
    return null;
};
