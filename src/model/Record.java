package model;

import javafx.beans.property.*;

/**
 * Класс для описания одной записи
 */
public class Record {
    private StringProperty plantName;//название завода
    private IntegerProperty byPlan;//потребление по плану
    private IntegerProperty inFact;//потребление по факту
    private IntegerProperty deviationW;//Отклонение в кВт
    private DoubleProperty deviationP;//Отклонение в процентах

    /**
     * Конструктор класса
     * @param plantName название завода
     * @param byPlan потребление по плану
     * @param inFact потребление по факту
     */
    public Record(String plantName, int byPlan, int inFact) {
        this.plantName = new SimpleStringProperty(plantName);
        this.byPlan = new SimpleIntegerProperty(byPlan);
        this.inFact = new SimpleIntegerProperty(inFact);
        calcDeviationW();
        calcDeviationP();
    }

    /**
     * Получение названия завода
     * @return название завода
     */
    public StringProperty getPlantName() {
        return plantName;
    }

    /**
     * Изменение названия завода
     * @param plantName новое название завода
     */
    public void setPlantName(String plantName) {
        this.plantName = new SimpleStringProperty(plantName);
    }

    /**
     * Получение значения потребления по плану
     * @return значение потребления по плану
     */
    public IntegerProperty getByPlan() {
        return byPlan;
    }

    /**
     * Изменение значения потребления по плану
     * @param byPlan новое значение потребления по плану
     */
    public void setByPlan(int byPlan) {
        this.byPlan = new SimpleIntegerProperty(byPlan);
    }

    /**
     * Получение значения потребления по факту
     * @return значение потребления по факту
     */
    public IntegerProperty getInFact() {
        return inFact;
    }

    /**
     * Изменение значения потребления по факту
     * @param inFact новое значение потребления по факту
     */
    public void setInFact(int inFact) {
        this.inFact = new SimpleIntegerProperty(inFact);
    }

    /**
     * Подсчет отклонения в кВт
     */
    private void calcDeviationW() {
        deviationW = new SimpleIntegerProperty(byPlan.get() - inFact.get());
    }

    /**
     * Получение значения отклонения в кВт
     * @return значение отклонения в кВт
     */
    public IntegerProperty getDeviationW() {
        return deviationW;
    }

    /**
     * Подсчет отклонения в процентах
     */
    private void calcDeviationP() {
        deviationP = new SimpleDoubleProperty(deviationW.get() * 100 / byPlan.get());
    }

    /**
     * Получение значения отклонения в процентах
     * @return значение отклонения в процентах
     */
    public DoubleProperty getDeviationP() {
        return deviationP;
    }

    /**
     * Переподсчет отклонений
     */
    public void recalc() {
        calcDeviationW();
        calcDeviationP();
    }
}
