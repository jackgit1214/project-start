import bindActionCreators from "react-redux/lib/utils/bindActionCreators";
import * as globalActions from "../reducers/GlobleReducers";
import {connect} from "react-redux";
import {Card, Layout, Progress,Table,Button} from "element-react";
import {useEffect} from "react";
import moment from "moment";
const DashboardStyle = {
	userInfo: {
		display: "flex",
		alignItems: "center",
		paddingBottom: "20px",
		borderBottom: "2px solid #ccc",
		marginBottom: "20px"
	},

	userAvatar: {
		width: "120px",
		height: "120px",
		borderRadius: "50%"
	},

	userInfoCont: {
		paddingLeft: "50px",
		flex: "1",
		fontSize: " 14px",
		color: "#999"
	},

	userInfoList: {
		fontSize: "14px",
		color: "#999",
		lineHeight: " 25px"
	}

}

const Dashboard = (props) => {

	const user = props.user;
	const userCount = props.sysUser;
	const deptCount = props.sysDept;
	const roleCount = props.sysRole;
	const logData = props.logData;
	const dispatch = props.dispatch;
	const dataFormat = (logDate)=>{
		return moment(logDate).format("YYYY-MM-DD HH:mm:ss");
	}
	const logColumns= [
		{
			type: "index",width: 180
		},
		{
			type: 'selection',prop: "logId",width: "120"
		},
		{
			label: "时间",
			prop: "logTime",
			width: 240,
			render: function(data){
				return (
					<span>
		            <i className="fa fa-calendar"></i>
		            <span style={{marginLeft: '10px'}}>{dataFormat(data.logTime)}</span>
		          </span>)
			}
		},
		{
			label: "描述",
			prop: "description",
			width: 180
		},
		{
			label: "操作",
			prop: "zip",
			fixed: 'right',
			width: 100,
			render: ()=>{
				return <span><Button type="text" size="small">查看</Button><Button type="text" size="small">编辑</Button></span>
			}
		}
	]
	useEffect(()=>{
		dispatch({type:"getHomeData"});
	},[])
	return (
		<div>
			<Layout.Row gutter={20}>
				<Layout.Col span={8}>
					<Card shadow="hover" style={{height: "252px", marginBottom: "20px"}}>
						<div style={DashboardStyle.userInfo}>
							{user.avatar != null ? <img style={DashboardStyle.userAvatar} src={user.avatarPic}/> :
								<img style={DashboardStyle.userAvatar} src="/images/user.jpg"/>}
							<div style={DashboardStyle.userInfoCont}>
								<div>{user.userName}</div>
								{/*<div>{{ userInfo.role() }}</div>*/}
							</div>
						</div>
						<div style={DashboardStyle.userInfoList}>
							上次登录时间：
							<span>2021-11-01</span>
						</div>
						<div style={DashboardStyle.userInfoList}>
							上次登录地点：
							<span>北京</span>
						</div>
					</Card>
					<Card shadow="hover" style={{height: "252px"}}
					      header={<div className="clearfix">
						      <span>语言详情</span>
					      </div>}>
						React
						<Progress percentage={71.3}></Progress>JavaScript
						<Progress percentage={24.1}></Progress>CSS
						<Progress percentage={13.7}></Progress>HTML
						<Progress percentage={5.9}></Progress>
					</Card>
				</Layout.Col>
				<Layout.Col span={16}>
					<Layout.Row gutter={20} style={{marginBottom: "20px"}}>
						<Layout.Col span={8}>
							<Card shadow="hover" bodyStyle={{padding: '0px'}}>
								<div className="grid-content grid-con-1">
									<i className="fa fa-user grid-con-icon"></i>
									<div className="grid-cont-right">
										<div className="grid-num">{userCount.count}</div>
										<div>用户数</div>
									</div>
								</div>
							</Card>
						</Layout.Col>
						<Layout.Col span={8}>
							<Card shadow="hover" bodyStyle={{padding: '0px'}}>
								<div className="grid-content grid-con-2">
									<i className="fa fa-key grid-con-icon"></i>
									<div className="grid-cont-right">
										<div className="grid-num">{roleCount.count}</div>
										<div>角色数</div>
									</div>
								</div>
							</Card>
						</Layout.Col>
						<Layout.Col span={8}>
							<Card shadow="hover" bodyStyle={{padding: '0px'}}>
								<div className="grid-content grid-con-3">
									<i className="fa fa-group grid-con-icon"></i>
									<div className="grid-cont-right">
										<div className="grid-num">{deptCount.count}</div>
										<div>组织结构</div>
									</div>
								</div>
							</Card>
						</Layout.Col>
					</Layout.Row>
					<Card shadow="hover" style={{height: "403px"}} header={<div className="clearfix">
						<span>系统日志</span>
					</div>}>
						<Table style={{width: '100%'}}
						       columns={logColumns}
						       height={350}
						       data={logData}>
						</Table>
					</Card>
				</Layout.Col>
			</Layout.Row>
		</div>
	)
}

const mapStateToProps = (state, ownProps) => {
	let global = state.global;
	let tmpUser = global.user;
	let tmpData = state.home.statisticData;
	console.log(tmpData)
	if (tmpUser != null) {
		tmpUser["message"] = 5
	}
	return {
		userInfo: global.loginInfo,
		user: tmpUser,
		logData:state.home.logData,
		sysUser:tmpData?tmpData["SysUser"]:{count:0},
		sysDept:tmpData?tmpData["SysDept"]:{count:0},
		sysRole:tmpData?tmpData["SysRole"]:{count:0},
	};
};

const mapDispatchToProps = dispatch => {
	const action = bindActionCreators(globalActions, dispatch);
	return {
		pageAction: action,
		dispatch,
	};
};

export default connect(
	mapStateToProps,
	mapDispatchToProps,
)(Dashboard);
