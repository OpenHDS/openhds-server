package org.openhds.domain.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.openhds.domain.model.AuditableEntity;

public class CheckEntityNotVoidedImpl implements ConstraintValidator<CheckEntityNotVoided, AuditableEntity> {

    private boolean allowNull;

    public void initialize(CheckEntityNotVoided arg0) {
        this.allowNull = arg0.allowNull();

    }

    public boolean isValid(AuditableEntity auditableEntity, ConstraintValidatorContext context) {

        if (allowNull && auditableEntity == null) {
            return true;
        }

        if (auditableEntity.isDeleted()) {
            return false;
        }
        return true;
    }
}
