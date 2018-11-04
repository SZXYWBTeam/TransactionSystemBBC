package cn.szxywb.web.bbc.bean.card;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * 订单匹配Card
 */
public class TransMatchCard {
    @Expose
    private List<TransactionCard> transCard;
    @Expose
    private List<TransactionCard> recommendTransCard;

    public TransMatchCard(List<TransactionCard> transCard, List<TransactionCard> recommendTransCard) {
        this.transCard = transCard;
        this.recommendTransCard = recommendTransCard;
    }

    public List<TransactionCard> getTransCard() {
        return transCard;
    }

    public void setTransCard(List<TransactionCard> transCard) {
        this.transCard = transCard;
    }

    public List<TransactionCard> getRecommendTransCard() {
        return recommendTransCard;
    }

    public void setRecommendTransCard(List<TransactionCard> recommendTransCard) {
        this.recommendTransCard = recommendTransCard;
    }
}
