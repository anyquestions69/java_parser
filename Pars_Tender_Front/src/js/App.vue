<template>
    <Header />
    <Main />
    <Footer />
</template>

<script>
import Header from './components/Header.vue';
import Main from './components/Main.vue';
import Footer from './components/Footer.vue';

export default {
    components: {
        Header,
        Main,
        Footer
    },
    data() {
        return {
            text: 'Hello world!'
        }
    },
}
</script>

<style>
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}
</style>