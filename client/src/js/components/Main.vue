<template>
<main class="main">
    <div class="main__into">
        <p class="p_1">.</p>
        <div class="table">
            <table >
                <caption class="table__logo">Таблица тендеров</caption>
                <thead class="thead">
                    <tr class="tr">
                        <th>№</th>
                        <th>Наименование</th>
                        <th>Код</th>
                        <th>Время начала</th>
                        <th>Время публикации</th>
                        <th>Время окончания</th>
                        <th>Цена</th>
                        <th>Ссылка</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(elem, index) in data" :key="index">
                        <td>{{ index }}</td>
                        <td>{{ elem["name"] }}</td>
                        <td>{{ elem["code"] }}</td>
                        <td>{{ elem["dateStart"] }}</td>
                        <td>{{ elem["datePublish"] }}</td>
                        <td>{{ elem["dateEnd"] }}</td>
                        <td>{{ elem["price"] }}</td>
                        <td>{{ elem["href"] }}</td>
                    </tr>
                </tbody>
            </table>
        </div>
        <p class="p_2">.</p>
    </div>
</main>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            data: []
        }
    },
    created() {
        this.fetchData();
    },
    methods: {
        async fetchData() {
            try {
                const response = await axios.get('http://localhost:8080/tender');
                this.data = response.data;
            } catch (error) {
                console.error('Ошибка при получении данных:', error);
            }
        }
    }
}
</script>

<style lang="scss">
%block {
    display: block;
    margin: 0 auto;
    width: 95%;
};

%flex-display {
    display: flex;
    justify-content: center;
    align-items: center;
};

.main {
    width: 100%;
    min-height: 100vh;
    background-blend-mode: multiply;
    background: url('../../assets/main_bg.jpg');
    background-color: rgb(101, 101, 101);
    background-size: cover;
    background-repeat: no-repeat;

    .main__into {
        @extend %block;
        gap: 20px;

        .p_1 {
            margin-bottom: 40px;
            font-size: 1px;
        }

        .table {
            padding: 20px 0;
            background: white;
            border-radius: 10px;
            width: 100%;
            box-shadow: 0 5px 15px rgba(2, 137, 161, 0.834);

            table {
            font-family: sans-serif;
            overflow-y: auto;
            scrollbar-width: none;
            border-collapse: collapse;

            .table__logo {
                font-size: 28px;
                text-align: center;
                margin-bottom: 20px;
                color: rgb(0, 0, 0);
            }

            .thead {
                background: rgb(0, 238, 255);
                height: 45px;

                .tr {
                    width: 100%;

                    th {
                        padding: 4px 2px;
    
                        &:nth-child(odd) {
                            background-color: rgb(17, 227, 242);
                        }
                    }
                }
            }

            tbody {

                tr {

                    height: 40px;
                    
                    &:nth-child(odd) {
                        background: rgb(244, 243, 243);
                        
                    }

                    td {

                        text-align: center;
                        padding: 5px 3px;
                        font-size: 14px;

                        &:not(:last-child) {
                            border-right: 1px solid rgb(183, 183, 183);
                        }
                    }

                    &:hover {
                        background: rgba(206, 232, 228, 0.737) !important;
                    }
                }
                
            }
            }
        };

        .p_2 {
            margin-top: 40px;
            font-size: 1px;
        }
    }
}
</style>