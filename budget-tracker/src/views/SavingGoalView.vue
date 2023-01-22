<template>
  <div>
    <div id="contentContainer" class="signupcontentContainer container rounded-lg mt-3 shadow-lg p-3 mb-5 bg-white rounded" data-intro="On this page, you can calculate a saving goal. Just provide the amount you want to save." data-step="1">
      <h2>Saving Goal:</h2>
      <h6 v-if="showSuccess">Saved successfully...</h6>
      <br />
      <div>
        <b-row class="lowerRow" v-if="showForm == false && title != ''" style="margin-bottom: 15px">
          <b-col lg="6">
            <label for="title" class="disabled-input-show">Title:</label>
            <b-form-input v-model="title" type="text" class="form-control" id="title" name="title" placeholder="No Saving-Goal yet!" disabled />
          </b-col>
          <b-col data-intro="This is the amount you need to save for your next goal" data-step="3">
            <label for="difference" label-align="left" class="disabled-input-show">Difference to Balance:</label>
            <b-form-input v-model="differenceToBalance" type="text" class="form-control" id="differenceToBalance" name="differenceToBalance" placeholder="difference To Balance" disabled />
          </b-col>
          <b-col>
            <label for="balance" label-align="left" class="disabled-input-show">Balance:</label>
            <b-form-input v-model="balanceToShow" type="text" class="form-control" id="balance" name="balance" placeholder="Balance" disabled />
          </b-col>
          </b-row>
          <b-row class="lowerRow" v-if="showForm == false && title != ''" style="margin-bottom: 15px">
          
          <b-col>
            <label for="amount" label-align="left" class="disabled-input-show">Amount:</label>
            <b-form-input v-model="amountToShow" type="text" class="form-control" id="amount" name="amount" placeholder="Amount" disabled />
          </b-col>
          <b-col data-intro="This is the predicted date, when your goal will be reached" data-step="4">
            <label for="date" label-align="left" class="disabled-input-show">Estimated Finish Date:</label>
            <b-form-input v-model="finishDate" type="text" class="form-control" id="finishDate" name="finishDate" placeholder="Finish Date" disabled />
          </b-col>
        </b-row>
        <b-row class="lowerRow" v-if="showForm == false">
          <b-col>
            <b-button @click="initSavingGoal()" class="btn btn-lg btg-dark btn-block" name="return" data-intro="Click here to create a new saving goal." data-step="2">Create Saving Goal</b-button>
          </b-col>
        </b-row>
      </div>
      <div class="form-signin" v-if="showForm">
        <b-form id="signupForm" v-on:submit.prevent="checkBeforeSubmit">
          <b-row>
            <b-col>
              <b-form-input v-model="newTitle" type="text" class="form-control" id="newTitle" name="newTitle" placeholder="Title" autofocus />
              <label v-if="showErrorMessageNewTitle === true" class="error-label" for="newTitle">{{ errorMessageNewTitle }}</label>
            </b-col>
          </b-row>
          <b-row style="margin-top: 10px">
            <b-col>
              <b-form-input v-model="newAmount" type="number" class="form-control" id="newAmount" name="newAmount" placeholder="Amount" />
              <label v-if="showErrorMessageNewAmount === true" class="error-label" for="newAmount">{{ errorMessageNewAmount }}</label>
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
export default {
  name: "SavingGoalView",
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
      showForm: false,
      title: "",
      amount: "",
      amountToShow: "",
      finishDate: "",
      newTitle: "",
      newAmount: "",
      showErrorMessageNewTitle: false,
      errorMessageNewTitle: "",
      showErrorMessageNewAmount: false,
      errorMessageNewAmount: "",
      incomeAndExpenses: [],
      statistic: [],
      balance: 0,
      balanceToShow: "",
      differenceToBalance: 0,
    };
  },
  async mounted() {
    await this.getSavingGoal();
  },
  methods: {
    redirect() {
      this.showSuccess = true;
      this.getSavingGoal();
      setTimeout(() => ((this.showSuccess = false), (this.showForm = false)), 1500);
    },
    triggerModal(modalBody) {
      this.modalBodyText = modalBody;
      this.$refs.modalName.openModal();
    },
    async getSavingGoal() {
      await this.$axios
        .get(this.$apiUrl + "/savinggoal/getSavingGoal", this.jwtConfig)
        .then((response) => {
          this.title = response.data.title;
          this.amount = response.data.amount;
          this.amountToShow = this.amount.toFixed(2) + " €";
          this.getIncomesAndExpenses();
        })
        .catch((error) => {
          console.log(error.response.data.message);
        });
    },
    initSavingGoal() {
      this.showForm = true;
    },
    async createSavingGoal() {
      await this.$axios
        .post(this.$apiUrl + "/savinggoal/setSavingGoal", { title: this.newTitle, amount: this.newAmount }, this.jwtConfig)
        // eslint-disable-next-line no-unused-vars
        .then((response) => {
          this.redirect();
        })
        .catch((error) => {
          console.log(error.response.data.message);
        });
    },
    checkBeforeSubmit() {
      this.showErrorMessageNewTitle = false;
      this.showErrorMessageNewAmount = false;
      if (this.newTitle === "") {
        this.showErrorMessageNewTitle = true;
        this.errorMessageNewTitle = "Please enter a title!";
      } else {
        this.showErrorMessageNewTitle = false;
      }
      if (this.newAmount === "") {
        this.showErrorMessageNewAmount = true;
        this.errorMessageNewAmount = "Please enter an amount!";
      } else {
        this.showErrorMessageNewAmount = false;
      }
      if (this.newTitle !== "" && this.newAmount !== "") {
        this.createSavingGoal();
      }
    },
    async getIncomesAndExpenses() {
      await this.$axios
        .get(this.$apiUrl + "/incomeAndExpenses", this.jwtConfig)
        .then((responseProducts) => {
          this.incomeAndExpenses = responseProducts.data;
          this.calculateStatistic();
        })
        .catch((error) => {
          console.log(error);
          if (localStorage.getItem("token") == null) {
            this.$router.push({ name: "login" });
          }
        });
    },
    calculateStatistic() {
      this.statistic = [];
      this.balance = 0;
      this.incomeAndExpenses.forEach((element) => {
        if (element.type == "INCOME") {
          this.balance += element.amount;
        } else {
          this.balance -= element.amount;
        }
        var month = element.date.split("-")[1];
        var year = element.date.split("-")[0];
        var found = false;
        this.statistic.forEach((element2) => {
          if (element2.month == year + "/" + month) {
            found = true;
            if (element.type == "INCOME") {
              element2.income += element.amount;
            } else {
              element2.expenses += element.amount;
            }
            element2.total = element2.income - element2.expenses;
          }
        });
        if (!found) {
          if (element.type == "INCOME") {
            this.statistic.push({
              month: year + "/" + month,
              income: element.amount,
              expenses: 0,
              total: element.amount,
            });
          } else {
            this.statistic.push({
              month: year + "/" + month,
              income: 0,
              expenses: element.amount,
              total: 0 - element.amount,
            });
          }
        }
      });
      this.differenceToBalance = (this.balance - this.amount).toFixed(2);
      this.balanceToShow = this.balance.toFixed(2) + " €";
      this.calaculateFinishDate();
    },
    calculateAvergageTotalPerMonth() {
      var total = 0;
      var date = new Date();
      var month = ("0" + (date.getMonth() + 1)).slice(-2);
      var year = date.getFullYear();
      this.statistic.forEach((element) => {
        if (element.month != year + "/" + month) {
          total += element.total;
        }
      });
      return (total / this.statistic.length).toFixed(2);
    },
    calculateAverageExpensesPerMonth() {
      var total = 0;
      var date = new Date();
      var month = ("0" + (date.getMonth() + 1)).slice(-2);
      var year = date.getFullYear();
      this.statistic.forEach((element) => {
        if (element.month != year + "/" + month) {
          total += element.expenses;
        }
      });
      return (total / this.statistic.length).toFixed(2);
    },
    calaculateFinishDate() {
      var date = new Date();
      var month = date.getMonth() + 1;
      var year = date.getFullYear();
      var daysInMonth = new Date(year, month, 0).getDate();
      var daysTillEndOfMonth = daysInMonth - date.getDate();
      var tempDifferenceToBalance = this.differenceToBalance - (this.calculateAverageExpensesPerMonth() / daysInMonth * daysTillEndOfMonth).toFixed(2);
      if(tempDifferenceToBalance > 0) {
        this.finishDate = "Goal reached!";
      } else {
        var daysTillFinish = Math.abs(tempDifferenceToBalance / (this.calculateAvergageTotalPerMonth() / 30));
        var finishDate = new Date();
        finishDate.setDate(finishDate.getDate() + daysTillFinish);
        this.finishDate = finishDate.toLocaleDateString();
      }
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

.form-control:disabled {
  background-color: #fff;
  color: #000;
  font-weight: 550;
  border: none;
}
.disabled-input-show {
  margin-bottom: 0px;
  float: left;
}
</style>
