<template>
    <header class="header">
        <div class="header__into">
            <div>
                <h2 class="logo">SmarTen</h2>
            </div>
            <div class="header__navigate">
                <nav class="header__nav">
                    <input type="text" placeholder="Поиск" class="header__input">
                </nav>
                <menu class="header__menu" title="Меню">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512" class="menu__icon">
                        <path d="M240.1 4.2c9.8-5.6 21.9-5.6 31.8 0l171.8 98.1L448 104l0 .9 47.9 27.4c12.6 7.2 18.8 22 15.1 36s-16.4 23.8-30.9 23.8H32c-14.5 0-27.2-9.8-30.9-23.8s2.5-28.8 15.1-36L64 104.9V104l4.4-1.6L240.1 4.2zM64 224h64V416h40V224h64V416h48V224h64V416h40V224h64V420.3c.6 .3 1.2 .7 1.8 1.1l48 32c11.7 7.8 17 22.4 12.9 35.9S494.1 512 480 512H32c-14.1 0-26.5-9.2-30.6-22.7s1.1-28.1 12.9-35.9l48-32c.6-.4 1.2-.7 1.8-1.1V224z"/>
                    </svg>
                </menu>
            </div>
        </div>
    </header>
</template>

<script>
export default {}
</script>

<style lang="scss">
@import url('https://fonts.googleapis.com/css2?family=Rubik+Scribble&display=swap');

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


.header {
    font-family: "Rubik Scribble", system-ui;
    font-weight: 400;
    font-size: 25px;
    font-style: normal;
    color: white;
    background: rgb(56, 56, 56);
    border-bottom: 3px solid rgb(224, 224, 224);
    height: 10vh;
    width: 100%;

    .header__into {
       @extend %block;
       @extend %flex-display;
       padding: 10px 0px;

       .header__nav {

            .header__input {
                outline: none;
                padding: 2px 5px;
                width: 200px;
                height: 30px;
                border: 1px solid silver;
                border-radius: 8px;
            }
       }

       .header__navigate {
            @extend %flex-display;
            width: 40%;

            .header__menu {
                width: 30px;
                height: 30px;
                cursor: pointer;
            }
       }
    }
}
</style>