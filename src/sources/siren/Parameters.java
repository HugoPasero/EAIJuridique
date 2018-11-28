/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sources.siren;

/**
 *
 * @author marieroca
 */
class Parameters {
    private String timezone;

    private String start;

    private String[] dataset;

    private String q;

    private String format;

    private String lang;

    private String rows;

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String[] getDataset() {
        return dataset;
    }

    public void setDataset(String[] dataset) {
        this.dataset = dataset;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "ClassPojo [timezone = " + timezone + ", start = " + start + ", dataset = " + dataset + ", q = " + q + ", format = " + format + ", lang = " + lang + ", rows = " + rows + "]";
    }
}
