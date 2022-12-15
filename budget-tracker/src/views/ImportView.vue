<template>
  <div>
    <ModalMain :title="modalTitleText" :body="modalBodyText" ref="modalName" />
  </div>
</template>

<script>
import ModalMain from "../components/modal-main.vue";
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
    };
  },
};
</script>

<style scoped>
</style>
