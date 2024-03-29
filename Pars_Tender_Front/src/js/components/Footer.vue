<template>
<footer class="footer">
    <div class="footer__into">
        <p class="footer__text">Все права защищены &copy;</p>
    </div>
</footer>
</template>

<script>
export default {}
</script>

<style lang="scss">
%block {
    display: block;
    margin: 0 auto;
    height: 100%;
    width: 90%;
};

%flex-display {
    display: flex;
    justify-content: space-between;
    align-items: center;
};

.footer {
    width: 100%;
    height: 10vh;
    background: rgb(56, 56, 56);
    color: white;
    border-top: 3px solid rgb(224, 224, 224);

    .footer__into {
        @extend %block;
        display: flex;
        justify-content: center;
        align-items: center;
        padding: 10px 0;

        .footer__text {
            font-size: 17px;
        }
    }
}
</style>