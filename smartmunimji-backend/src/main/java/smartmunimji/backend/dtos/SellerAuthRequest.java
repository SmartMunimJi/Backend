package smartmunimji.backend.dtos;

public class SellerAuthRequest {
    private String sellersEmail;
    private String password;

    public String getSellersEmail() {
        return sellersEmail;
    }

    public void setSellersEmail(String sellersEmail) {
        this.sellersEmail = sellersEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}