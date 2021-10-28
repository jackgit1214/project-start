import {connect, useDispatch, useSelector} from "react-redux";
import bindActionCreators from "react-redux/lib/utils/bindActionCreators";
import {Card, Layout,Tree,Table,Button} from "element-react";
import {useEffect} from "react";
import department, {actionType} from "../../reducers/department";

const DepartmentTree = (props) =>{

	const options= {
		children: 'child',
		label: 'deptName',
		id:"deptId"
	}
	const departments = props.departments;
	const dispatch = props.dispatch;
	useEffect(()=>{
		dispatch({
			type: actionType.DEPARTMENT_PAGE_DATA
		})
	},[])
	return (
		<Tree
			style={{height:500}}
			data={departments}
			options={options}
			nodeKey="id"
			highlightCurrent={true}
			defaultExpandedKeys={["1"]}
			defaultCheckedKeys={[props.currentNodeId]}
			onNodeClicked={(data, reactElement,)=>{
				dispatch({
					type:"updateUserState",
					properties:{"currentDept":data}
				})
			}}
		/>
	)
}

const getChildData =(depts,superId)=>{
	const findStr = (department) => department.superId === superId;

	let result = [];
	const index = depts.findIndex(findStr);
	if (index===-1) return result;

	depts.map((department)=>{
		if (department.superId===superId){
			department.child = getChildData(depts,department.deptId);
			result.push(department);
		}
	});
	return result;
}
const mapStateToProps = (state, ownProps) => {
	let pageData = state.department;
	let departments = pageData.deptTree;
	let deptTreeData =[];
	if(departments.length>0){
		deptTreeData = getChildData(departments,"xxx");
	}
	return {
		departments: deptTreeData,
		currentNodeId:ownProps.currentNodeId
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
)(DepartmentTree);