package pe.vizcacha.laboratorio.service;

/**
 * Regla de negocio incumplida. El mensaje es literal del §6 y viaja tal cual
 * al frontend, que lo muestra en el Notice del formulario o, en el caso de
 * las transiciones bloqueadas, en un toast de advertencia (§5.4).
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
