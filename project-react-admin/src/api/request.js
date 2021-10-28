import {baseUrl, clientProperties, tokenUrl} from './env'
import {getStore} from "./mUtils";
const request = (url, method, params) => {
	let truesUrl = baseUrl + url;
	let type = method.toUpperCase();
	let sysToken = JSON.parse(getStore("token"));
	let requestConfig = {
		method,
		headers: {
			'Content-Type': "application/json",
			"Authorization":sysToken.token_type +" "+ sysToken.access_token
		},
	}
	if (params !=undefined){
		if ((type == 'GET' || type=='DELETE')) {
			let queryStr = '';
			Object.keys(params).forEach(key => {
				queryStr += key + '=' + params[key] + '&';
			})
			if (queryStr !== '') {
				queryStr = queryStr.substr(0, queryStr.lastIndexOf('&'));
				truesUrl = truesUrl + '?' + queryStr;
			}
		}else{
			requestConfig.append("body",JSON.stringify(params))
		}
	}

	return new Promise((resolve, reject) => {
		fetch(truesUrl, requestConfig)
			.then((response) => {
				return response.json();
			})
			.then((responseData) => {
				resolve(responseData);
			})
			.catch((error) => {
				reject(error);
			});
	});
};

const fetchToken = (loginInfo) => {
	let formData = new FormData();
	formData.append("username", loginInfo.userName);
	formData.append("password", loginInfo.password);
	formData.append("grant_type", clientProperties.grant_type);
	formData.append("client_id", clientProperties.client_id);
	formData.append("client_secret", clientProperties.client_secret);
	formData.append("scope", clientProperties.scope);
	let requestConfig = {
		method: "post",
		mode: 'cors',
		body: formData
	}
	return new Promise((resolve, reject) => {
		fetch(tokenUrl, requestConfig)
			.then((response) => {
				let result = response.json();
				if (response.ok){
					return result;
				}
				throw new Error(response.status+":授权错误，请检查用户名密码！或与系统管理员联系。");
			})
			.then((responseData) => {
				responseData.status = true;
				resolve(responseData)
			})
			.catch((error) => {
				resolve({
					status:false,
					errorInfo:error.message
				})
			});
	});

}

export {
	request,
	fetchToken
};
