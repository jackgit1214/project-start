import '../assets/css/login.css';
import {useState, useEffect,useRef }  from "react"
import {Button, Form, Input} from "element-react";
import { useHistory,useLocation } from 'react-router-dom';
import {connect} from 'react-redux'
import bindActionCreators from "react-redux/lib/utils/bindActionCreators";
import * as loginActions from "../reducers/GlobleReducers"

function Login(props) {

	const pageAction = props.pageAction
	const loginForm = useRef(null);
	const token = props.globalInfo.token;
	const history = useHistory();
	const location = useLocation();
	const [loginInfo,setLoginInfo] = useState({
		form:{
			userName:"",
			password:""
		},
		rules: {
			userName: [
				{ required: true, message: '请输入登录名称', trigger: 'blur' }
			],
			password: [
				{ required: true, message: '请输入用户密码', trigger: 'change' }
			],
		}
	})

	useEffect(() => { //监控token的变化
		if (token!=null && token!=""){
			history.push(location)
			history.replace("/")
		}
	}, [token]);
	const handleSubmit = (e)=>{
		loginForm.current.validate((valid) => {
			if (valid) {
				pageAction.systemLogin(loginInfo.form);
			} else {
				return false;
			}
		});
	}
	const handleChange = (key, value)=>{
		setLoginInfo({
			form:Object.assign(loginInfo.form,{ [key]: value }),
			rules: loginInfo.rules
		});
	}
	return (
		<div className="login-wrap">
			<div className="ms-login">
				<div className="ms-title">后台管理系统</div>
				<Form ref={loginForm} className="ms-content" model={loginInfo.form} rules={loginInfo.rules} >
					<Form.Item prop="userName" >
						<Input value={loginInfo.form.userName} onChange={(value)=>{
							handleChange("userName",value);
						}}  prepend={
							<Button type="primary"><i className="fa fa-user"></i></Button>
						}></Input>
					</Form.Item>
					<Form.Item prop="password" >
						<Input  type="password" value={loginInfo.form.password} onChange={(value)=>{
							handleChange("password",value);
						}}  prepend={
							<Button  type="primary"><i className="fa fa-lock"></i></Button>
						}></Input>
					</Form.Item>
					<Form.Item className="login-btn">
						<Button type="primary" onClick={(e)=>{
							handleSubmit(e)}} >登录</Button>
					</Form.Item>
				</Form>
			</div>
		</div>
	);
}

const mapStateToProps = (state) => {
	return {
		globalInfo: state.global,
	};
};

const mapDispatchToProps = dispatch => {
	const action = bindActionCreators(loginActions, dispatch);
	return {
		pageAction:action,
		dispatch,
	};
};

export default connect(
	mapStateToProps,
	mapDispatchToProps,
)(Login);
