import { tokenUrl,clientProperties } from './env'
import {setStore,getStore,removeStore } from "./mUtils";

const fetchToken = async(username,password) =>{
	let formData = new FormData();
	formData.append("username",username);
	formData.append("password",password);
	formData.append("grant_type",clientProperties.grant_type);
	formData.append("client_id",clientProperties.client_id);
	formData.append("client_secret",clientProperties.client_secret);
	formData.append("scope",clientProperties.scope);
	let requestConfig = {
		method:"post",
		mode:'cors',
		body:formData
	}
	try {
		const response = await fetch(tokenUrl, requestConfig);
		//const responseJson = await response.json();
		return response
	} catch (error) {
		console.log(error);
		throw new Error(error)
	}
}

export const loginAndGetToken = async(username,password) =>{
	let response = await fetchToken(username,password);
	if (response.status==200){
		let sysToken = await response.json();
		sysToken.startTime = new Date().getTime();
		setStore("sysToken",sysToken);
		sysToken.status = response.status;
		return sysToken;
	}else{
		let errorInfo = await response.json()
		errorInfo.status = response.status;
		return await errorInfo;
	}
}

/**
 * 根据用户名与密码，获取token
 */
export const getToken = async() => {
	let sysToken = JSON.parse(getStore("sysToken"));
	if (!sysToken)
		return "";
	if (checkTokenExp(sysToken)){
		sysToken = await refreshToken(sysToken);
		sysToken.startTime = new Date().getTime();
		console.log("=============刷新token ,时间重置=======================");
		console.log(sysToken);
		console.log(sysToken.startTime);
		setStore("sysToken",sysToken);
	}
	return sysToken.token_type +" "+ sysToken.access_token;
}

export  const checkTokenExp = (sysToken) =>{

	if (sysToken==undefined || sysToken==""||sysToken==null){
		sysToken =  JSON.parse(getStore("sysToken"));
	}
	if (sysToken==undefined || sysToken==""||sysToken==null){ //无token 过期了
		console.log("无token ...................")
		return true;
	}
	// console.log(sysToken)
	let currentTime = new Date().getTime();
	// console.log("curtime:"+currentTime);
	// console.log("tokentime:"+sysToken.startTime);
	// console.log(currentTime - sysToken.startTime);
	// console.log(sysToken.expires_in * 1000);
	if ((currentTime - sysToken.startTime) > (sysToken.expires_in*1000)) { //过期了
		console.log("过期了.........................")
		return true;
	}
	return false;
}

/**
 * 刷新token
 */
export const refreshToken = async(sysToken) => {
	let formData = new FormData();
	formData.append("grant_type","refresh_token");
	formData.append("client_id",clientProperties.client_id);
	formData.append("client_secret",clientProperties.client_secret);
	formData.append("refresh_token",sysToken.refresh_token);
    // console.log("....................refresh_token......................")
	let requestConfig = {
		method:"post",
		mode:'cors',
		body:formData
	}
	try {
		const response = await fetch(tokenUrl, requestConfig);
		const responseJson = await response.json();
		//setStore("sysToken",responseJson);
		return responseJson
	} catch (error) {
		console.log(error);
		throw new Error(error)
	}
}

