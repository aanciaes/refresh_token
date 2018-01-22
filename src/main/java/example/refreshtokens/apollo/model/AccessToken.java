package example.refreshtokens.apollo.model;

public class AccessToken {

    private String accessToken;
    private String type;

    public AccessToken() {
    }

    public AccessToken(String accessToken, String type) {
        this.accessToken = accessToken;
        this.type = type;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
