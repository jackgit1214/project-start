import { baseUrl } from './env'
import {getToken} from "./tokenHandler";

const config = {
	credentials: 'include',
	headers: {
		'Accept': '*/*',
		'Content-Type': 'application/json',
	},
	mode: "cors",
	cache: "force-cache"
}

async function resetConfig(type) {
	const token = await getToken();

	let requestConfig = JSON.parse(JSON.stringify(config));
	requestConfig.method = type;
	requestConfig.headers["Authorization"] = token;
	return requestConfig;
}

export default async(url = '', data = {}, type = 'GET',isFormData=false, method = 'fetch') => {
	type = type.toUpperCase();
	url =  baseUrl+ url;
	let requestConfig = await resetConfig(type);
	// console.log(requestConfig);
	let formData = new FormData();
	if (isFormData){ //组织formdata
		Object.keys(data).forEach((key) => {
			if (data[key]==null || data[key]=="null")
				formData.append(key, "");
			else
				formData.append(key,data[key])
		});
	}

	if (type == 'GET' || type=='DELETE') {
		let dataStr = ''; //数据拼接字符串
		Object.keys(data).forEach(key => {
			dataStr += key + '=' + data[key] + '&';
		})
		if (dataStr !== '') {
			dataStr = dataStr.substr(0, dataStr.lastIndexOf('&'));
			url = url + '?' + dataStr;
		}
	}else{
		if (isFormData){
			Object.defineProperty(requestConfig, 'body', {
				value: formData
			})
			//formdata提交移除contenttype,由系统指定
			delete requestConfig.headers["Content-Type"]
		}else{
			Object.defineProperty(requestConfig, 'body', {
				value: JSON.stringify(data)
			})
		}
	}
	if (window.fetch && method == 'fetch') {
		try {
			const response = await fetch(url, requestConfig);
			const responseJson = await response.json();
			return responseJson
		} catch (error) {
			throw new Error(error)
		}
	} else {
		return new Promise((resolve, reject) => {
			let requestObj;
			if (window.XMLHttpRequest) {
				requestObj = new XMLHttpRequest();
			} else {
				requestObj = new ActiveXObject;
			}

			let sendData = '';
			if (type == 'POST') {
				sendData = JSON.stringify(data);
			}

			requestObj.open(type, url, true);
			requestObj.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
			requestObj.send(sendData);

			requestObj.onreadystatechange = () => {
				if (requestObj.readyState == 4) {
					if (requestObj.status == 200) {
						let obj = requestObj.response
						if (typeof obj !== 'object') {
							obj = JSON.parse(obj);
						}
						resolve(obj)
					} else {
						reject(requestObj)
					}
				}
			}
		})
	}
}