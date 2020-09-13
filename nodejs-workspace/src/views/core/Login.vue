<template>
    <div class="login-container">
        <div style="height:30px"></div>
        <h1>欢迎登录</h1>
        
        <div class="form">
            <div class="form-group">
                <input v-model="form.authenticator" :style="form.authenticator" type="text" class="form-control form-control-authenticator" placeholder="请输入登录账号 / 手机号码 / 邮箱" />
            </div>
            <div class="form-group">
                <input v-model="form.certification" :style="form.certification" type="password" class="form-control form-control-certification" placeholder="请输入密码" />
            </div>
            <button type="button" class="form-button" @click="handleSubmit">登录</button>
        </div>

        <div class="copyright">
            <small>Copyright 奇思妙想工作室 1.0.0 &copy; 2020</small>
        </div>
    </div>
</template>

<script>
import md5 from "js-md5";
import redirect from "utils/redirect";

export default {
    data(){
        return {
            form: {
                authenticator: "",
                certification: ""
            },
            style: {
                authenticator: {borderColor: "#1ab394"},
                certification: {borderColor: "#F00"}
            },
            pending: false
        };
    },
    methods: {
        handleSubmit() {
            let authenticator = this.form.authenticator;
            let certification = this.form.certification;
            if(!(authenticator && authenticator.length > 0)) {
                this.$Message.error("请输入登录身份，它可以是登录账号 / 手机号码 / 邮箱");
                return;
            }
            if(!(certification && certification.length > 0)) {
                this.$Message.error("请输入登录密码");
                return;
            }
            if(certification.length < 4) {
                this.$Message.error("不是有效的登录密码");
                return;
            }
            certification = md5(certification);
            this.pending = true;
            API.login({
                authenticator,
                certification
            }).then((response) => {
                this.$Message.success("登录成功，正在跳转...");
                redirect("/?reason=login-succeed&path=/login&t=" + Date.now());
                this.pending = false;
            }).catch(err => this.$error(err, () => {
                this.pending = false;
            }));
        }
    }
}
</script>

<style>
html, body {
    height: 100%;
    min-height: 400px;
    position: relative;
}

.login-container {
    height: 100%;
    width: 100%;
    text-align: center;
    position: relative;
}

h1 {
    line-height: 2em;
}

.form {
    width: 300px;
    margin: 80px auto 0px auto;
}
.form-group {
    margin: 10px auto;
}
.form-control {
    width: 100%;
    color: inherit;
    display: block;
    padding: 6px 12px;
    border-radius: 3px;
    background-image: none;
    border: 1px solid #e5e6e7;
    background-color: #FFFFFF;
    transition: border-color 0.15s ease-in-out 0s, box-shadow 0.15s ease-in-out 0s;
}
.form-control:focus {
    outline: 0;
    border-color: #1ab394;
}
.form-button {
    color: #fff;
    background-color: #1ab394;
    border-color: #1ab394;
    display: inline-block;
    font-weight: 400;
    text-align: center;
    white-space: nowrap;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
    border: 1px solid transparent;
    padding: .375rem .75rem;
    font-size: 1rem;
    line-height: 1.5;
    border-radius: .25rem;
    overflow: visible;
    cursor: pointer;
    display: block !important;
    width: 100% !important;
    border-radius: 3px;
    font-size: inherit;
    transition: color .15s ease-in-out,background-color .15s ease-in-out,border-color .15s ease-in-out,box-shadow .15s ease-in-out;
}
.form-button:hover {
    outline: 0;
    color: #fff;
    background-color: #2cbda0;
    border-color: #2cbda0;
}
.form-button:active {
    outline: 0;
    background-color: #138c73;
    border-color: #138c73;
}
.form-button:focus {
    outline: 0;
    background-color: #138c73;
    border-color: #138c73;
}

.copyright {
    bottom: 0;
    width: 100%;
    font-size: 12px;
    line-height: 2em;
    color: darkgray;
    position: absolute;
    text-align: center;
    border-top: darkgray 1px solid;
}
</style>