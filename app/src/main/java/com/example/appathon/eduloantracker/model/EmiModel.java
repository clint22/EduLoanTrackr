package com.example.appathon.eduloantracker.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by navneet on 8/4/17.
 */

public class EmiModel {

        @SerializedName("code")
        @Expose
        private String code;
        @SerializedName("No_of_EMIs")
        @Expose
        private String noOfEMIs;
        @SerializedName("EMI_Dates")
        @Expose
        private String eMIDates;
        @SerializedName("Last_three_EMIs")
        @Expose
        private String lastThreeEMIs;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getNoOfEMIs() {
            return noOfEMIs;
        }

        public void setNoOfEMIs(String noOfEMIs) {
            this.noOfEMIs = noOfEMIs;
        }

        public String getEMIDates() {
            return eMIDates;
        }

        public void setEMIDates(String eMIDates) {
            this.eMIDates = eMIDates;
        }

        public String getLastThreeEMIs() {
            return lastThreeEMIs;
        }

        public void setLastThreeEMIs(String lastThreeEMIs) {
            this.lastThreeEMIs = lastThreeEMIs;
        }


}
