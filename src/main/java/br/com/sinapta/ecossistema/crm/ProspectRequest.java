package br.com.sinapta.ecossistema.crm;

import jakarta.validation.constraints.NotBlank;

public record ProspectRequest(
        @NotBlank String companyName,
        String document,
        String contactName,
        String contactPhone,
        String contactEmail,
        String notes
) {
}
