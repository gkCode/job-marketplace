import React, { Component } from "react";
import {
  getAllProjects,
  getUserCreatedProjects
} from "./../../util/APIUtils";
import LoadingIndicator from "../../common/LoadingIndicator";
import { Button, Icon, notification } from "antd";
import { PROJECT_LIST_SIZE } from "../../constants/index";
import { withRouter } from "react-router-dom";
import "./ProjectList.css";
import { Card } from "antd";

class ProjectInfo extends Component {
    constructor(props){
        super(props);
        this.state = {
          
        };
    }

    componentWillMount() {
        this.loadProjectInfo();
    }

    componentWillReceiveProps(nextProps) {
        if(this.props.isAuthenticated !== nextProps.isAuthenticated) {
            // Reset State
            this.setState({
                projects: [],
                isLoading: false
            });    
            this.loadProjectInfo();
        }
    }

    render() {
        return (
        <div className="project-grid">
            <Card title="Card title">Card content</Card>
        </div>
        );
    }
}

export default withRouter(ProjectInfo);
