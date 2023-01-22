<template>
  <div>
    <div id="contentContainer" class="signupcontentContainer container rounded-lg mt-3 shadow-lg p-3 mb-5 bg-white rounded">
      <h2>Users:</h2>
      <br />
      <div>
        <b-row>
          <b-table
            id="my-table"
            :items="usersTable"
            :fields="fieldsUsers"
            :per-page="perPage"
            :current-page="currentPage"
            :sort-by.sync="sortBy"
            :sort-desc.sync="sortDesc"
            :show-sort-icons="true"
            striped
            responsive
            small
            label-sort-asc=""
            label-sort-desc=""
            label-sort-clear=""
            ><template #cell(active)="row">
              <span v-if="row.item.active == true"><b-btn class="btn-active-custom" @click="editActive(row.item.id)">Active</b-btn></span>
              <span v-else><b-btn class="btn-inactive-custom" @click="editActive(row.item.id)">Inactive</b-btn></span>
            </template>
          </b-table>
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
        </b-row>
        <br />
      </div>
    </div>
    <ModalMain :title="modalTitleText" :body="modalBodyText" ref="modalName" />
  </div>
</template>

<script>
import ModalMain from "../components/modal-main.vue";
export default {
  name: "UsersView",
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
      fieldsUsers: [
        { key: "id", label: "ID", sortable: true, tdClass: 'align-middle' },
        { key: "name", label: "Name", sortable: true, tdClass: 'align-middle' },
        { key: "email", label: "Email", sortable: true, tdClass: 'align-middle' },
        { key: "admin", label: "User Type", sortable: true, formatter: "formatAdmin", tdClass: 'align-middle' },
        { key: "active", label: "Status", sortable: true, tdClass: 'align-middle' },
      ],
      usersTable: [],
      currentPage: 1,
      perPage: 10,
      rows: 0,
      sortBy: "id",
      sortDesc: false,
    };
  },
  async mounted() {
    await this.getUsers();
  },
  methods: {
    redirect() {
      setTimeout(() => this.$router.push("/"), 1000);
    },
    triggerModal(modalBody) {
      this.modalBodyText = modalBody;
      this.$refs.modalName.openModal();
    },
    async editActive(userIdToChange) {
      await this.$axios
        .post(this.$apiUrl + "/auth/changeUserActiveById", { id: userIdToChange }, this.jwtConfig)
        // eslint-disable-next-line no-unused-vars
        .then((response) => {
          this.getUsers();
        })
        .catch((error) => {
          console.log(error);
          this.triggerModal("Error while changing user status");
        });
    },
    async getUsers() {
      await this.$axios
        .get(this.$apiUrl + "/auth", this.jwtConfig)
        .then((response) => {
          this.usersTable = response.data;
          this.rows = response.data.length;
          this.adaptTable();
        })
        .catch((error) => {
          console.log(error);
          this.triggerModal("Error while getting users");
        });
    },
    adaptTable() {
      this.usersTable.forEach((element) => {
        element.name = element.firstName + " " + element.lastName;
      });
    },
    formatAdmin(value) {
      return value == true ? "Admin" : "User";
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

.btn-active-custom {
  background-color: #28a745 !important;
  border-color: #28a745 !important;
  color: white !important;
}

.btn-active-custom:hover {
  background-color: #218838 !important;
  border-color: #1e7e34 !important;
  color: white !important;
}

.btn-inactive-custom {
  background-color: #dc3545 !important;
  border-color: #dc3545 !important;
  color: white !important;
}

.btn-inactive-custom:hover {
  background-color: #c82333 !important;
  border-color: #bd2130 !important;
  color: white !important;
}
</style>
