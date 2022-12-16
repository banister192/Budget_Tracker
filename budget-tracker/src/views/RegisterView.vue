<template>
  <div id="contentContainer" class="signupcontentContainer container rounded-lg mt-3 shadow-lg p-3 mb-5 bg-white rounded">
    <h4 class="text-center">Register</h4>
    <h6 v-if="showSuccess">Registration successfully...</h6>
    <br />
    <div class="form-signin" v-if="showForm">
      <b-form id="signupForm" v-on:submit.prevent="signup">
        <b-row>
          <b-col>
            <b-form-input v-model="email" type="email" class="form-control" id="email" name="email" placeholder="E-Mail" autofocus />
            <label v-if="showErrorMessageUsername === true" class="error-label" for="username">{{ errorMessageUsername }}</label>
          </b-col>
          <!--
          <b-col>
            <b-form-input v-model="username" type="text" class="form-control" id="username" name="username" placeholder="Username" />
          </b-col>
          -->
        </b-row>
        <br />
        <b-row>
          <b-col>
            <b-form-input v-model="firstName" type="text" class="form-control" id="firstname" name="firstname" placeholder="Firstname" />
            <label v-if="showErrorMessageFirstname === true" class="error-label" for="username">{{ errorMessageFirstname }}</label>
          </b-col>
          <b-col>
            <b-form-input v-model="lastName" type="text" class="form-control" id="lastname" name="lastname" placeholder="Lastname" />
            <label v-if="showErrorMessageLastname === true" class="error-label" for="username">{{ errorMessageLastname }}</label>
          </b-col>
        </b-row>
        <br />
        <b-form-input v-model="password1" type="password" class="form-control" id="password" name="password" placeholder="Password" /><br />
        <b-form-input v-model="password2" type="password" class="form-control" id="password2" name="password2" placeholder="Confirm password" />
        <label v-if="showErrorMessagePassword === true" class="error-label" for="password">{{ errorMessagePassword }}</label
        ><br />
        <b-button class="btn btn-lg btg-dark btn-block" type="submit" name="login">Submit</b-button>
      </b-form>
      <br />
      <label>Already have an account? <router-link to="/login">Login Here!</router-link></label>
    </div>
    <ModalMain :title="modalTitleText" :body="modalBodyText" ref="modalName" />
  </div>
</template>

<script>
import ModalMain from "../components/modal-main.vue";
export default {
  name: "RegisterView",
  components: {
    ModalMain,
  },
  data() {
    return {
      modalTitleText: "ERROR",
      modalBodyText: "",
      showSuccess: false,
      showForm: true,
      showErrorMessageUsername: false,
      showErrorMessagePassword: false,
      showErrorMessageFirstname: false,
      showErrorMessageLastname: false,
      errorMessageUsername: "Error Message",
      errorMessagePassword: "Error Message",
      errorMessageFirstname: "Error Message",
      errorMessageLastname: "Error Message",
      email: "",
      firstName: "",
      lastName: "",
      password1: "",
      password2: "",
    };
  },
  methods: {
    triggerModal(modalBody) {
      this.modalBodyText = modalBody;
      this.$refs.modalName.openModal();
    },
    redirect() {
      setTimeout(() => this.$router.push("/login"), 1000);
    },
    signup() {
      if (!this.validateInput(this.email, this.firstName, this.lastName, this.password1, this.password2)) {
        return;
      }
      this.$axios
        .post(this.$apiUrl + "/auth/register", {
          email: this.email,
          firstName: this.firstName,
          lastName: this.lastName,
          password: this.password1,
        })
        .then(() => {
          this.showForm = false;
          this.showSuccess = true;
          this.redirect();
        })
        .catch((error) => {
          this.triggerModal(error.response.data.status + ": " + error.response.data.error);
          console.log(error);
        });
    },
    validateInput(email, firstName, lastName, password1, password2) {
      let valid = true;
      if (password1 === "") {
        this.showErrorMessagePassword = true;
        this.errorMessagePassword = "Password is required!";
        valid = false;
      } else if (password1 !== password2) {
        this.showErrorMessagePassword = true;
        this.errorMessagePassword = "Passwords do not match!";
        valid = false;
      }
      if (email !== "") {
        let emailRegex = new RegExp("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,4}$");
        if (!emailRegex.test(email)) {
          this.showErrorMessageUsername = true;
          this.errorMessageUsername
          valid = false;
        }
      } else {
        this.showErrorMessageUsername = true;
        this.errorMessageUsername = "Email is required!";
        valid = false;
      }
      if (firstName === "") {
        this.showErrorMessageFirstname = true;
        this.errorMessageFirstname = "Firstname is required!";
        valid = false;
      }
      if (lastName === "") {
        this.showErrorMessageLastname = true;
        this.errorMessageLastname = "Lastname is required!";
        valid = false;
      }
      return valid;
    },
  },
};
</script>

<style>
.form-signup {
  width: 100%;
  max-width: 630px;
  padding: 15px;
  margin: auto;
}

#loginForm .custom-control-label {
  margin-left: 7px;
}
</style>

<style scoped>
h6 {
  color: green;
  margin-top: 20px;
}

.error-label {
  margin-top: 0px;
  margin-left: 10px;
  float: left;
  margin-bottom: 10px;
  color: red;
}
</style>