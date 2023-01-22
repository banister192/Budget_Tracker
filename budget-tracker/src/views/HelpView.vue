<template>
  <div id="contentContainer" class="container rounded-lg mt-3 shadow-lg p-3 mb-5 bg-white rounded">
    <h2>{{ Page_title }}</h2>
    <br />
    <div class="accordion" role="tablist">
      <b-card no-body class="mb-1">
        <b-card-header header-tag="header" class="p-1" role="tab">
          <b-button block v-b-toggle.accordion-1 variant="info">{{ Accordion1_title }}</b-button>
        </b-card-header>
        <b-collapse id="accordion-1" visible accordion="my-accordion" role="tabpanel">
          <b-card-body>
            <b-card-text>{{ Accordion1_text }}</b-card-text>
          </b-card-body>
        </b-collapse>
      </b-card>
      <b-card no-body class="mb-1">
        <b-card-header header-tag="header" class="p-1" role="tab">
          <b-button block v-b-toggle.accordion-2 variant="info">{{ Accordion2_title }}</b-button>
        </b-card-header>
        <b-collapse id="accordion-2" accordion="my-accordion" role="tabpanel">
          <b-card-body>
            <b-card-text>{{ Accordion2_text }}</b-card-text>
          </b-card-body>
        </b-collapse>
      </b-card>
      <b-card no-body class="mb-1">
        <b-card-header header-tag="header" class="p-1" role="tab">
          <b-button block v-b-toggle.accordion-3 variant="info">{{ Accordion3_title }}</b-button>
        </b-card-header>
        <b-collapse id="accordion-3" accordion="my-accordion" role="tabpanel">
          <b-card-body>
            <b-card-text>{{ Accordion3_text }}</b-card-text>
          </b-card-body>
        </b-collapse>
      </b-card>
      <b-card no-body class="mb-1">
        <b-card-header header-tag="header" class="p-1" role="tab">
          <b-button block v-b-toggle.accordion-4 variant="info">{{ Accordion4_title }}</b-button>
        </b-card-header>
        <b-collapse id="accordion-4" accordion="my-accordion" role="tabpanel">
          <b-card-body>
            <b-card-text>{{ Accordion4_text }}</b-card-text>
          </b-card-body>
        </b-collapse>
      </b-card>
      <b-card no-body class="mb-1">
        <b-card-header header-tag="header" class="p-1" role="tab">
          <b-button block v-b-toggle.accordion-5 variant="info">{{ Accordion5_title }}</b-button>
        </b-card-header>
        <b-collapse id="accordion-5" accordion="my-accordion" role="tabpanel">
          <b-card-body>
            <b-card-text>{{ Accordion5_text }}</b-card-text>
          </b-card-body>
        </b-collapse>
      </b-card>
      <b-card no-body class="mb-1">
        <b-card-header header-tag="header" class="p-1" role="tab">
          <b-button block v-b-toggle.accordion-6 variant="info">{{ Accordion6_title }}</b-button>
        </b-card-header>
        <b-collapse id="accordion-6" accordion="my-accordion" role="tabpanel">
          <b-card-body>
            <b-card-text>{{ Accordion6_text }}</b-card-text>
          </b-card-body>
        </b-collapse>
      </b-card>
      <b-card no-body class="mb-1">
        <b-card-header header-tag="header" class="p-1" role="tab">
          <b-button block v-b-toggle.accordion-7 variant="info">{{ Accordion7_title }}</b-button>
        </b-card-header>
        <b-collapse id="accordion-7" accordion="my-accordion" role="tabpanel">
          <b-card-body>
            <b-card-text>{{ Accordion7_text }}</b-card-text>
          </b-card-body>
        </b-collapse>
      </b-card>
      <b-card no-body class="mb-1">
        <b-card-header header-tag="header" class="p-1" role="tab">
          <b-button block v-b-toggle.accordion-8 variant="info">{{ Accordion8_title }}</b-button>
        </b-card-header>
        <b-collapse id="accordion-8" accordion="my-accordion" role="tabpanel">
          <b-card-body>
            <b-card-text>{{ Accordion8_text }}</b-card-text>
          </b-card-body>
        </b-collapse>
      </b-card>
      <b-card no-body class="mb-1">
        <b-card-header header-tag="header" class="p-1" role="tab">
          <b-button block v-b-toggle.accordion-9 variant="info">{{ Accordion9_title }}</b-button>
        </b-card-header>
        <b-collapse id="accordion-9" accordion="my-accordion" role="tabpanel">
          <b-card-body>
            <b-card-text>{{ Accordion9_text }}</b-card-text>
          </b-card-body>
        </b-collapse>
      </b-card>
      <b-card no-body class="mb-1">
        <b-card-header header-tag="header" class="p-1" role="tab">
          <b-button block v-b-toggle.accordion-10 variant="info">{{ Accordion10_title }}</b-button>
        </b-card-header>
        <b-collapse id="accordion-10" accordion="my-accordion" role="tabpanel">
          <b-card-body>
            <b-card-text>{{ Accordion10_text }}</b-card-text>
          </b-card-body>
        </b-collapse>
      </b-card>
      <b-card no-body class="mb-1">
        <b-card-header header-tag="header" class="p-1" role="tab">
          <b-button block v-b-toggle.accordion-11 variant="info">{{ Accordion11_title }}</b-button>
        </b-card-header>
        <b-collapse id="accordion-11" accordion="my-accordion" role="tabpanel">
          <b-card-body>
            <b-card-text>{{ Accordion11_text }}</b-card-text>
          </b-card-body>
        </b-collapse>
      </b-card>
      <b-card no-body class="mb-1">
        <b-card-header header-tag="header" class="p-1" role="tab">
          <b-button block v-b-toggle.accordion-12 variant="info">{{ Accordion12_title }}</b-button>
        </b-card-header>
        <b-collapse id="accordion-12" accordion="my-accordion" role="tabpanel">
          <b-card-body>
            <b-card-text>{{ Accordion12_text }}</b-card-text>
          </b-card-body>
        </b-collapse>
      </b-card>
    </div>
  </div>
</template>

<script>
export default {
  name: "HelpView",
  data() {
    return {
      Page_title: "Help/FAQ",
      Accordion1_title: "How can I use this app?",
      Accordion1_text: `Just navigate to any site and click on "Help" at the bottom. This will open an overlay with the help content.`,
      Accordion2_title: "What's a good target for my savings?",
      Accordion2_text: `This is a common question with a ton of different answers — it differs for everyone. A good rule of thumb for your emergency fund is to save up enough to cover your expenses for 3–6 months. This might sound ambitious, but it’s a good goal to build into your plan.`,
      Accordion3_title: "Why is a budget important?",
      Accordion3_text: `All this budgeting info might have you asking, well, why? It’s a fair question.
By planning ahead for large (and even unexpected) expenses in life, you’ll have the money you need no matter what comes up — without resorting to expensive credit cards and personal loans.
As a result, you’ll pay a lot less when those things happen, avoid falling into a hole financially, and you’ll have a lot more peace of mind in your day-to-day life.`,
      Accordion4_title: "What’s the best way to stick to my budget?",
      Accordion4_text: `The most important thing you can do to stick to your budget is to make sure it’s realistic. If you’re just starting out on your personal finance journey and your one-year goal is to amass a net worth of 1.3 billion USD, you might want to realign and re-evaluate that objective.
How? By including all of your expenses, saving for the unexpected, and making sure the amounts in your budget really meet your needs. And also by including doing what you love!
People often think budgeting is about miserly hoarding every single dime, but guess what? It really isn’t.
It’s more about long-term planning. A good budget sets aside some money for savings and paying down debt, includes enough to cover your bills, and still gives you some spending money left over.
If you keep running into unexpected bills, add more to your savings every month to cover them. If you’re spending more than you intended on entertainment, try to budget more for that.
Your budget is unique to you. Restructure it until it’s working for what you really need.`,
      Accordion5_title: "How do I create a budget?",
      Accordion5_text: `You can create a budget in the Budget Tracker by clicking the “New Budget” button in the navigation bar. This will open a form where you can enter your budget information, including budget categories, budget items, and budget limits. Once you have entered all the information, click “Save” to save your budget.`,
      Accordion6_title: "How do I add transactions to my budget?",
      Accordion6_text: `To add transactions to your budget, click on the “Add Transaction” button in the navigation bar. Enter the transaction date, amount, budget item, and other relevant information. Click “Save” to complete the process.`,
      Accordion7_title: "How do I delete a budget item?",
      Accordion7_text: `To delete a budget item, click on the “Delete” button next to the item in the budget list. This will delete the item from your budget and all associated transactions.`,
      Accordion8_title: "How do I view my budget history?",
      Accordion8_text: `To view your budget history, click on the “History” button in the navigation bar. This will display a graph of your budget over time, as well as a detailed list of all of your transactions.`,
      Accordion9_title: "How can I set budget limits?",
      Accordion9_text: `To set budget limits, click on the “Limits” button in the navigation bar. Enter the maximum budget amount for each category and click “Save” to complete the process.`,
      Accordion10_title: "How do I view my budget in different currencies?",
      Accordion10_text: `To view your budget in different currencies, click on the “Currencies” button in the navigation bar. Select the currency you would like to view and click “Save” to complete the process.`,
      Accordion11_title: "How do I export my budget data?",
      Accordion11_text: `To export your budget data, click on the “Export” button in the navigation bar. You can choose to export your data as a CSV, Excel, or PDF file.`,
      Accordion12_title: "How do I set up recurring payments?",
      Accordion12_text: `To set up recurring payments, click on the “Scheduled” button in the navigation bar. Enter the recurring payment amount, date, and budget item. Click “Save” to complete the process.`,
    };
  },
};
</script>

<style>
.btn-info {
  background-color: transparent !important;
  border: none;
  border-radius: 0px;
  text-decoration: none;
  background: linear-gradient(to right, rgba(100, 200, 200, 1), rgba(100, 200, 200, 1)),
    linear-gradient(to right, rgba(255, 0, 0, 1), rgba(255, 0, 180, 1), rgba(0, 100, 200, 1));
  background-size: 100% 3px, 0 3px;
  background-position: 100% 100%, 0 100%;
  background-repeat: no-repeat;
  transition: background-size 400ms;
}

.btn-info:hover {
  background-size: 0 3px, 100% 3px;
}
</style>
