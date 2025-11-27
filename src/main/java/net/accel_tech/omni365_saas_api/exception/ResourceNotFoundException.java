package net.accel_tech.omni365_saas_api.exception;

/**
 * @author NdourBamba18
 **/

public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
