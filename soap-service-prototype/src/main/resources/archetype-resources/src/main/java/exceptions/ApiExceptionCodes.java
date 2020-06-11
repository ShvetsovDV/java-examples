package ${package}.exceptions;

public enum ApiExceptionCodes {
    SERVICE_NETWORK_EXCEPTION("Service network exception! Please contact our administrator!"),
    SERVER_VALIDATE_EXCEPTION("Server validate failure! Please check parameters into xml!"),
    //ошибка сервера приложений. Обратиться к администратору приложения для получения логов, затем к разработчику.
    INTERNAL_SERVER_ERROR("Internal server error! Please contact our developer!");

    private String description;

    ApiExceptionCodes(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}