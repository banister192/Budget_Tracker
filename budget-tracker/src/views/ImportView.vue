<template>
  <div>
    <div id="contentContainer" class="signupcontentContainer container rounded-lg mt-3 shadow-lg p-3 mb-5 bg-white rounded">
      <h4 class="text-center">Import</h4>
      <h6 v-if="showSuccess">Import successfully...</h6>
      <br />
      <div v-if="showLoading">
        <div class="lds-roller">
          <div></div>
          <div></div>
          <div></div>
          <div></div>
          <div></div>
          <div></div>
          <div></div>
          <div></div>
        </div>
      </div>
      <div class="form-signin" v-if="showForm">
        <!--<b-form id="importForm" v-on:submit.prevent="importCSV">-->
        <b-form id="importForm">
          <b-form-select v-model="selected" :options="options"></b-form-select>
          <b-form-file
            id="csvUploadInput"
            name="file"
            accept=".csv"
            placeholder="Choose a CSV file..."
            drop-placeholder="Drop file here..."
            style="margin-top: 10px;"
            @change="importCSV"
          ></b-form-file>
          <!--<b-button class="btn btn-lg btg-dark btn-block btn-import-custom" type="submit" name="import">Submit</b-button>-->
        </b-form>
      </div>
      <div v-if="showTable">
        <b-table
          id="my-table"
          :items="parsedJSON"
          :fields="fields"
          :per-page="perPage"
          :current-page="currentPage"
          :sort-by.sync="sortBy"
          :sort-desc.sync="sortDesc"
          :show-sort-icons="true"
          :custom-foot="foot"
          sort-icon-left
          striped
          responsive
          hover
          small
          label-sort-asc=""
          label-sort-desc=""
          label-sort-clear=""
        ></b-table>
        <b-pagination
          class="customPagination"
          v-model="currentPage"
          :total-rows="rows"
          :per-page="perPage"
          aria-controls="my-table"
          size="sm"
          pills
          align="center"
        ></b-pagination>
        <b-button @click="postToBackend()" class="btn btn-lg btg-dark btn-block btn-return-custom" name="return">POST</b-button>
      </div>
    </div>
    <ModalMain :title="modalTitleText" :body="modalBodyText" ref="modalName" />
  </div>
</template>

<script>
import ModalMain from "../components/modal-main.vue";
//import VuePapaParse from "vue-papa-parse";
export default {
  name: "ImportView",
  components: {
    ModalMain,
  },
  data() {
    return {
      jwtConfig: {
        headers: {
          Authorization: "Bearer " + localStorage.getItem("token"),
        },
      },
      modalTitleText: "ERROR",
      modalBodyText: "",
      showSuccess: false,
      showForm: true,
      showTable: false,
      showLoading: false,
      selected: null,
      rows: 0,
      perPage: 14,
      currentPage: 1,
      sortBy: "date",
      sortDesc: true,
      options: [
        { value: null, text: "Please select a bank" },
        { value: "ersteBank", text: "Erste Bank" },
        { value: "bankAustria", text: "Bank Austria" },
        { value: "raiffeisen", text: "Raiffeisen" },
      ],
      parsedJSON: [],
      parseConfig: [],
      file: null,
      parsedData: null,
      parsed: false,
      fields: [
        { key: "amount", label: "Amount", sortable: true, class: "amountCustomClass" },
        { key: "title", label: "Title", sortable: true },
        { key: "date", label: "Date", sortable: true, formatter: this.formatDate },
      ],
      foot: [
        {
          amount: "Total",
          title: this.totalAmount,
          category: "",
          date: "",
        },
      ],
    };
  },
  watch: {
    parsedData(current) {
      if (current) {
        this.parseJSONCustom(current.data);
      }
    },
  },
  methods: {
    redirect() {
      this.showSuccess = true;
      this.showTable = false;
      this.showLoading = false;
      setTimeout(() => this.$router.push("/"), 1000);
    },
    triggerModal(modalBody) {
      this.modalBodyText = modalBody;
      this.$refs.modalName.openModal();
    },
    formatDate(value) {
      if (!value) return "";
      return new Date(value).toLocaleDateString("de-DE", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
      });
    },
    parseFile() {
      console.log(this.parseConfig);
      this.parsedData = this.$papa.parse(this.file, this.parseConfig);
    },
    async importCSV() {
      if (!this.getConfigFromInput()) {
        return;
      }
      this.showForm = false;
      this.showLoading = true;
      const file = document.querySelector("#csvUploadInput").files[0];
      const reader = new FileReader();
      reader.onloadend = () => {
        this.file = reader.result;
        this.parseFile();
      };
      reader.readAsText(file);
    },
    async postToBackend() {
      this.parsedJSON.forEach((row) => {
        //const [day, month, year] = row.date.split(".");
        //const tempDate = new Date(+year, month - 1, +day);
        row.dateISO = row.date.toISOString();
      });
      this.$axios
        .post(this.$apiUrl + "/incomeAndExpenses", this.parsedJSON, this.jwtConfig)
        .then((response) => {
          this.redirect();
          console.log(response);
        })
        .catch((error) => {
          this.triggerModal("Something went wrong");
          console.log(error);
        });
    },
    parseJSONCustom() {
      console.log(this.parsedData.data);
      if (this.selected === "ersteBank") {
        this.parsedData.data.forEach((row) => {
          const [day, month, year] = row.buchungsdatum.split(".");
          const tempDate = new Date(+year, month - 1, +day);
          if (row.partnername == "" || row.partnername == null || row.partnername == undefined) {
            if (row.buchungs_details == "" || row.buchungs_details == null || row.buchungs_details == undefined) {
              row.partnername = "No title";
            } else {
              row.partnername = row.buchungs_details;
            }
          }
          this.parsedJSON.push({
            date: tempDate,
            amount: row.betrag,
            title: row.partnername,
          });
        });
      } else if (this.selected === "bankAustria") {
        this.parsedData.data.forEach((row) => {
          const [day, month, year] = row.buchungsdatum.split(".");
          const tempDate = new Date(+year, month - 1, +day);
          if (row.auftraggebername == "" || row.auftraggebername == null || row.auftraggebername == undefined) {
            if (row.buchungstext == "" || row.buchungstext == null || row.buchungstext == undefined) {
              row.auftraggebername = "No title";
            } else {
              row.auftraggebername = row.buchungstext;
            }
          }
          this.parsedJSON.push({
            date: tempDate,
            amount: row.betrag,
            title: row.auftraggebername,
          });
        });
      } else if (this.selected === "raiffeisen") {
        this.parsedData.data.forEach((row) => {
          const [day, month, year] = row[0].split(".");
          const tempDate = new Date(+year, month - 1, +day);
          // get the text out of row[1] after "Zahlungsreferenz: " or "Zahlungsempfänger: "
          if (row[1].startsWith("Zahlungsreferenz: ")) {
            row[1] = row[1].split("Zahlungsreferenz: ")[1];
          } else if (row[1].startsWith("Zahlungsempfänger: ")) {
            row[1] = row[1].split("Zahlungsempfänger: ")[1];
          } else if (row[1].startsWith("INTERNET-Überweisung Empfänger: ")) {
            row[1] = row[1].split("INTERNET-Überweisung Empfänger: ")[1];
          } else if (row[1].startsWith("Verwendungszweck: ")) {
            row[1] = row[1].split("Verwendungszweck: ")[1];
          } else if (row[1].startsWith("Auftraggeber: ")) {
            row[1] = row[1].split("Auftraggeber: ")[1];
          } else if (row[1] == "" || row[1] == null || row[1] == undefined) {
            row[1] = "No title";
          }
          this.parsedJSON.push({
            date: tempDate,
            amount: row[3],
            title: row[1],
          });
        });
      }
      console.log(this.parsedJSON);
      this.showLoading = false;
      this.parsed = true;
      this.showForm = false;
      this.rows = this.parsedJSON.length;
      this.showTable = true;
      console.log("showTable = true");
    },
    getConfigFromInput() {
      if (this.selected === "ersteBank") {
        this.parseConfig = {
          delimiter: ",",
          header: true,
          dynamicTyping: true,
          skipEmptyLines: true,
          transformHeader: (header) => header.toLowerCase().replace(/\W/g, "_"),
        };
      } else if (this.selected === "bankAustria") {
        this.parseConfig = {
          delimiter: ";",
          header: true,
          dynamicTyping: true,
          skipEmptyLines: true,
          transformHeader: (header) => header.toLowerCase().replace(/\W/g, "_"),
        };
      } else if (this.selected === "raiffeisen") {
        this.parseConfig = {
          delimiter: ";",
          header: false,
          dynamicTyping: true,
          skipEmptyLines: true,
        };
      } else {
        this.triggerModal("Please select a bank!");
        return false;
      }
      return true;
    },
  },
};
</script>

<style scoped>
.btn-import-custom {
  margin-top: 10px;
}
</style>
