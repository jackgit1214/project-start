import {Tooltip,Dropdown} from "element-react";
import {useState,useEffect} from "react";
import {useSelector, useDispatch, useStore, connect} from 'react-redux'
import bindActionCreators from "react-redux/lib/utils/bindActionCreators";
import * as globalActions from "../reducers/GlobleReducers";


const OperationIcon = (props)=>{
	const collapse = props.status;
	if (collapse) {
		return <i className="fa fa-outdent"></i>;
	}
	return <i className="fa fa-indent"></i>;
}

const Header = (props) => {
	const pageAction = props.pageAction;
	const collapseStatus = useSelector((state => state.global.collapseStatus))
	const userInfo = props.userInfo;
	const user = props.user;
	const dispatch = props.dispatch;
	useEffect(()=>{
		dispatch({type:"getUserInfo",userInfo})
	},[])
	const collapseChange = () => {
		// 暂时取消，elmentUi-react 不支持菜单的展开与缩回
		// dispatch({ type: 'switchSlider' })
	};
	const handleCommand =(command)=>{
		if (command=="logout"){
			pageAction.systemLogout();
		}else if(command=="user"){

		}
	}
	return (
		<div className="header" >
			<div className="collapse-btn" onClick={collapseChange}>
				<OperationIcon status={collapseStatus} />
			</div>
			<div className="logo">信息系统基础管理平台</div>
			<div className="header-right">
				<div className="header-user-con">
					<div className="btn-bell">
						<Tooltip effect="dark"  placement="left" content={ user.message>0?"有"+user.message+"条未读消息":""}>
							<i className="fa fa-bell-o"></i>
						</Tooltip>
						<span className={user.message>0?"btn-bell-badge":""}></span>
					</div>
					<div className="user-avatar">
						{user.avatar!=null?<img src={user.avatarPic}/>:<img  src="/images/user.jpg" />}
					</div>
					<Dropdown onCommand={handleCommand} className="user-name" trigger="click" menu={(
						<Dropdown.Menu>
							<Dropdown.Item command="pwdEdit">密码修改</Dropdown.Item>
							<Dropdown.Item command="user">个人中心</Dropdown.Item>
							<a href="https://github.com/jackgit1214/project-start" target="_blank">
								<Dropdown.Item divided={true}>项目仓库</Dropdown.Item>
							</a>
							<Dropdown.Item divided={true} command="logout">退出登录</Dropdown.Item>
						</Dropdown.Menu>
					)}>
						<span className="el-dropdown-link">
						    {userInfo.userName}
							<i className="fa fa-caret-down"></i>
                    </span>
					</Dropdown>
				</div>
			</div>
		</div>
	)
}

const mapStateToProps = (state,ownProps) => {
	let global =  state.global;
	let tmpUser = global.user;
	if (tmpUser!=null){
		tmpUser["message"] = 5
	}
	return {
		userInfo: global.loginInfo,
		user:tmpUser,
	};
};

const mapDispatchToProps = dispatch => {
	const action = bindActionCreators(globalActions, dispatch);
	return {
		pageAction:action,
		dispatch,
	};
};

export default connect(
	mapStateToProps,
	mapDispatchToProps,
)(Header);
