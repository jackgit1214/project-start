
import { combineReducers } from 'redux';
// import dataTypes from './dataTypes';
import globalData from './GlobleReducers'
import HomeData from "./home";
import UserReducers from "./user";
import DepartmentReducers from "./department";
const rootReducer = combineReducers({

    // data: dataTypes,
    department:DepartmentReducers,
    user:UserReducers,
    home:HomeData,
    global:globalData
});

export default rootReducer;
