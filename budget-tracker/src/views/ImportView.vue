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
        <h5 class="text-center">Select a CSV file to import</h5>
        <!--<b-form id="importForm" v-on:submit.prevent="importCSV">-->
        <b-form id="importForm">
          <b-form-select v-model="selected" :options="options"></b-form-select>
          <b-form-file
            id="csvUploadInput"
            name="file"
            accept=".csv"
            placeholder="Choose a CSV file..."
            drop-placeholder="Drop file here..."
            style="margin-top: 10px"
            @change="importCSV"
          ></b-form-file>
          <!--<b-button class="btn btn-lg btg-dark btn-block btn-import-custom" type="submit" name="import">Submit</b-button>-->
        </b-form>
        <br />
        <h5 class="text-center">OR</h5>
        <br />
        <b-button @click="initManualImport()" class="btn btn-lg btg-dark btn-block" name="return">Create MANUAL</b-button>
      </div>
      <div v-if="showTable">
        <b-button @click="postToBackend()" class="btn btn-lg btg-dark btn-block btn-import-custom-table" name="return">POST</b-button>
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
      </div>

      <div class="form-signin" v-if="showManualForm">
        <b-form id="signupForm" v-on:submit.prevent="manualImport">
          <b-row>
            <b-col>
              <b-form-input v-model="newTitle" type="text" class="form-control" id="newTitle" name="newTitle" placeholder="Title" autofocus />
              <label v-if="showErrorMessageNewTitle === true" class="error-label" for="newTitle">{{ errorMessageNewTitle }}</label>
            </b-col>
          </b-row>
          <b-row style="margin-top: 10px">
            <b-col>
              <b-form-input v-model="newAmount" type="number" step="0.01" class="form-control" id="newAmount" name="newAmount" placeholder="Amount" />
              <label v-if="showErrorMessageNewAmount === true" class="error-label" for="newAmount">{{ errorMessageNewAmount }}</label>
            </b-col>
            <b-col>
              <b-form-datepicker v-model="newDate" type="date" class="form-control" id="newDate" name="newDate" placeholder="Date" />
              <label v-if="showErrorMessageNewDate === true" class="error-label" for="newDate">{{ errorMessageNewDate }}</label>
            </b-col>
          </b-row>
          <b-row style="margin-top: 10px">
            <b-col cols="6">
              <b-form-select v-model="selectedManualImportCategory" :options="optionsManualImportCategory"></b-form-select>
              <label v-if="showErrorMessageNewCategory === true" class="error-label" for="newCategory">{{ errorMessageNewCategory }}</label>
            </b-col>
            <b-col>
              <b-form-checkbox v-model="newIsAbo" style="border: 0" class="form-control" id="newIsAbo" name="newIsAbo" placeholder="Date">Is an Abo</b-form-checkbox>
            </b-col>
            <b-col>
              <b-form-select v-model="selectedManualImportAbo" :options="optionsManualImportAbo"></b-form-select>
              <label v-if="showErrorMessageNewAbo === true" class="error-label" for="newAbo">{{ errorMessageNewAbo }}</label>
            </b-col>
          </b-row>
          <b-button class="btn btn-lg btg-dark btn-block btn-import-custom" type="submit" name="import">Submit</b-button>
        </b-form>
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
      //showForm: false,
      showForm: true,
      showTable: false,
      showLoading: false,
      showManualForm: false,
      //showManualForm: true,
      showErrorMessageNewTitle: false,
      showErrorMessageNewAmount: false,
      showErrorMessageNewDate: false,
      showErrorMessageNewCategory: false,
      showErrorMessageNewAbo: false,
      errorMessageNewTitle: "",
      errorMessageNewAmount: "",
      errorMessageNewDate: "",
      errorMessageNewCategory: "",
      errorMessageNewAbo: "",
      selected: null,
      selectedManualImportCategory: null,
      selectedManualImportAbo: null,
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
      optionsManualImportCategory: [
        { value: null, text: "Please select a Category" },
        { value: "FOOD", text: "Food" },
        { value: "CLOTHES", text: "Clothes" },
        { value: "ENTERTAINMENT", text: "Entertainment" },
        { value: "HOUSING", text: "Housing" },
        { value: "TRANSPORTATION", text: "Transportation" },
        { value: "HEALTH", text: "Health" },
        { value: "EDUCATION", text: "Education" },
        { value: "SAVINGS", text: "Savings" },
        { value: "SALARY", text: "Salary" },
        { value: "OTHER", text: "Other" },
      ],
      optionsManualImportAbo: [
        { value: null, text: "Select Abo Interval" },
        { value: 12, text: "Monthly" },
        { value: 4, text: "Quarterly" },
        { value: 2, text: "Half Yearly" },
        { value: 1, text: "Yearly" },
      ],
      newTitle: "",
      newAmount: "",
      newDate: "",
      newIsAbo: false,
      newCategory: "",
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
      this.showForm = false;
      this.showManualForm = false;
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
    initManualImport() {
      this.showForm = false;
      this.showManualForm = true;
    },
    manualImport() {
      this.showErrorMessageNewTitle = false;
      this.showErrorMessageNewAmount = false;
      this.showErrorMessageNewDate = false;
      this.showErrorMessageNewCategory = false;
      this.showErrorMessageNewAbo = false;
      if (this.checkManualImportForm()) {
        this.showLoading = true;
        this.showManualForm = false;
        this.$axios
          .post(
            this.$apiUrl + "/incomeAndExpenses/createSingle",
            {
              title: this.newTitle,
              amount: this.newAmount,
              date: this.newDate,
              category: this.selectedManualImportCategory,
              abo: this.newIsAbo,
              aboInterval: this.selectedManualImportAbo,
            },
            this.jwtConfig
          )
          // eslint-disable-next-line no-unused-vars
          .then((response) => {
            this.showLoading = false;
            this.showSuccess = true;
            this.redirect();
          })
          .catch((error) => {
            this.showLoading = false;
            this.showForm = true;
            this.triggerModal(error.response.data.message);
          });
      }
    },
    checkManualImportForm() {
      let returnValue = true;
      if (this.newTitle === "") {
        this.showErrorMessageNewTitle = true;
        this.errorMessageNewTitle = "Please enter a title";
        returnValue = false;
      }
      if (this.newAmount === "") {
        this.showErrorMessageNewAmount = true;
        this.errorMessageNewAmount = "Please enter a amount";
        returnValue = false;
      }
      if (this.newDate === "") {
        this.showErrorMessageNewDate = true;
        this.errorMessageNewDate = "Please enter a date";
        returnValue = false;
      }
      if (this.selectedManualImportCategory === null) {
        this.showErrorMessageNewCategory = true;
        this.errorMessageNewCategory = "Please select a category";
        returnValue = false;
      }
      if (this.newIsAbo === true && this.selectedManualImportAbo === null) {
        this.showErrorMessageNewAbo = true;
        this.errorMessageNewAbo = "Please select an abo interval";
        returnValue = false;
      }
      return returnValue;
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
h6 {
  color: green;
  margin-top: 20px;
}

.btn-import-custom {
  margin-top: 10px;
}

.btn-import-custom-table {
  margin-bottom: 10px;
}

.error-label {
  margin-top: 0px;
  margin-left: 10px;
  float: left;
  margin-bottom: 10px;
  color: red;
}
</style>
