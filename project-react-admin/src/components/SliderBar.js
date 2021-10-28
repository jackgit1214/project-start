import { Menu } from "element-react";
import { Link,NavLink } from 'react-router-dom';
import bindActionCreators from "react-redux/lib/utils/bindActionCreators";
import * as globalActions from "../reducers/GlobleReducers";
import {connect,useDispatch,useSelector} from "react-redux";
import {useEffect,useState} from "react";

const renderMenuItem = (item) => (
	<Menu.Item index={item.id} key={item.id}>
		<NavLink to={item.urlLink} style={{color:"#bfcbd9"}} activeStyle={{
			fontWeight: "bold",
			color: "white",
		}} >
			<i className={item.icon}></i><span>{item.title}</span>
		</NavLink>
	</Menu.Item>
);

const renderSubMenu = (menu) => {
	return (
		<Menu.SubMenu key={menu.id} index={menu.id}
			title={<span><i className={menu.icon}></i>{menu.title}</span>}>
			{menu.subs.map((sub) => (sub.subs ? renderSubMenu(sub) : renderMenuItem(sub)))}
		</Menu.SubMenu>
	);
};

const SliderBar = (props) => {
	const pageAction = props.pageAction;
	const userInfo = props.userInfo;
	const menus = props.menu;
	// const [menus,setMenus] = useState([]);
	useEffect(() => {
		pageAction.retrieveMenuData(userInfo);
		// setMenus(props.menu)
	},[]);

	return (
		<div className="sidebar">
			<Menu defaultActive="2" theme="dark"
			      className="sidebar-el-menu"  unique-opened>
				{menus.map((menu, key) => (
					menu.subs? renderSubMenu(menu): renderMenuItem(menu)
				))}
			</Menu>
		</div>
	)
}


const generateItems = (moduleData) => {
	let tempItem = []
	moduleData.forEach(item => {
		let itemData = {};
		itemData.icon = 'fa ' + item.funIcon;
		itemData.title = item.funName;
		itemData.urlLink = item.urlLink;
		itemData.id = item.funId;
		if (item.child && item.child.length > 0)
			itemData.subs = generateItems(item.child)
		tempItem.push(itemData);
	});
	return tempItem;
}
// export default SliderBar;

const mapStateToProps = (state,ownProps) => {
	let global =  state.global;
	return {
		userInfo: global.loginInfo,
		menu:generateItems(global.menuData),
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
)(SliderBar);
