/**
 * 用户数据模型
 */
export interface UserModel {
	userId: string,
	userName: string,
	valid: number,
	name: string,
	password: string,
	gender: number,
	age: number,
	avatar: string,
	birthday: string,
	qq: string,
	email: string,
	address: string,
	phone: string,
	userOrder: number,
	userStatus: number
	avatarPic:string,
}

export const DefaultUserModel: UserModel = {
	address: "",
	age: 0,
	avatar: "",
	avatarPic: "",
	birthday: "",
	email: "",
	gender: 0,
	name: "",
	password: "",
	phone: "",
	qq: "",
	userName: "",
	userOrder: 0,
	userStatus: 0,
	valid: 0,
	userId: "",
}