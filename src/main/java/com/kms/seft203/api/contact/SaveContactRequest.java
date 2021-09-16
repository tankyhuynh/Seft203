package com.kms.seft203.api.contact;

import com.kms.seft203.domain.contact.Contact;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SaveContactRequest extends Contact {
}
