import {connect, useDispatch, useSelector} from "react-redux";
import bindActionCreators from "react-redux/lib/utils/bindActionCreators";
import {Card, Layout, Progress,Table,Button} from "element-react";
import {useEffect} from "react";
import DepartmentTree from "../../components/common/DepartmentTree";
const UserPage = (props) =>{
	const dispatch = props.dispatch;
	const dept = props.currentDept;

	useEffect(()=>{
		dispatch({
			type:"getPageData",
			page:0,
			limit:10,
			departmentId:dept.deptId?dept.deptId:""
		})
	},[dept])
	return (
		<div>
			<Layout.Row gutter={10}>
				<Layout.Col span={6} >
					<DepartmentTree currentNodeId={dept.deptId?dept.deptId:"1"}/>
				</Layout.Col>
				<Layout.Col span={18}>
					<div style={{backgroundColor:"white"}}>
						<Table
							style={{width: '100%'}}
							columns={props.columns}
							data={props.user.pageDatas}
							stripe={true}
						/>
					</div>
				</Layout.Col>
			</Layout.Row>
		</div>
	)
}


const mapStateToProps = (state, ownProps) => {

	const tableColumns= [
		{type: "index",width: 180},
		{type: 'selection',prop: "userId",width: "120"},
		{label: "登录名",prop: "username",width: 240,},
		{label: "姓名",prop: "name",width: 240,},
		{label: "操作",prop: "zip",fixed: 'right',width: 100,
			render: ()=>{
				return <span><Button type="text" size="small">查看</Button><Button type="text" size="small">编辑</Button></span>
			}
		}
	]

	let pageData = state.user;
	return {
		user: pageData.user,
		columns:tableColumns,
		currentDept:pageData.currentDept
	};
};

const mapDispatchToProps = dispatch => {
	// const action = bindActionCreators(globalActions, dispatch);
	return {
		dispatch,
	};
};

export default connect(
	mapStateToProps,
	mapDispatchToProps,
)(UserPage);
