import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogModule, MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';

export interface DialogField {
  name: string;
  label: string;
  type: 'text' | 'email' | 'number' | 'select' | 'textarea';
  required?: boolean;
  options?: { value: any, label: string }[];
  initialValue?: any;
  value?: any;
}

@Component({
  selector: 'app-create-dialog',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule
  ],
  templateUrl: './create-dialog.html',
  styleUrl: './create-dialog.scss'
})
export class CreateDialog {
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<CreateDialog>,
    @Inject(MAT_DIALOG_DATA) public data: { title: string, fields: DialogField[] }
  ) {
    const group: any = {};
    data.fields.forEach(field => {
      const validators = field.required ? [Validators.required] : [];
      if (field.type === 'email') validators.push(Validators.email);
      
      const val = (field.value !== undefined && field.value !== null) ? field.value : 
                  (field.initialValue !== undefined && field.initialValue !== null) ? field.initialValue : '';
                  
      group[field.name] = [val, validators];
    });
    this.form = this.fb.group(group);
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  onSubmit(): void {
    if (this.form.valid) {
      this.dialogRef.close(this.form.value);
    }
  }
}
