package pl.sp33dster.githubrepos.dto;

public record ErrorResponse(
        int status,
        String message
) {}
