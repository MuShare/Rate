package edu.ut.softlab.rate.bean;

import java.util.List;
import java.util.Map;

/**
 * Created by alex on 16-8-5.
 */
public class SubscribeSyncBean {
    private List<SubscribeBean> createdOrUpdated;
    private List<String> deletedSubcribes;
    private Map<String, Double> rates;

    public List<SubscribeBean> getCreatedOrUpdated() {
        return createdOrUpdated;
    }

    public void setCreatedOrUpdated(List<SubscribeBean> createdOrUpdated) {
        this.createdOrUpdated = createdOrUpdated;
    }

    public List<String> getDeletedSubcribes() {
        return deletedSubcribes;
    }

    public void setDeletedSubcribes(List<String> deletedSubcribes) {
        this.deletedSubcribes = deletedSubcribes;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }
}
