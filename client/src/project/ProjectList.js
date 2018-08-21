import React, { Component } from "react";
import {
  getAllProjects,
  getUserPlacedBids
} from "../util/APIUtils";
import LoadingIndicator from "../common/LoadingIndicator";
import { Button, Icon, notification } from "antd";
import { PROJECT_LIST_SIZE } from "../constants";
import { withRouter } from "react-router-dom";
import "./ProjectList.css";
import { Grid, TableView, Table , TableHeaderRow} from "@devexpress/dx-react-grid-material-ui";

class ProjectList extends Component {
    constructor(props){
        super(props);
        this.state = {
            rows: '',
            columns: '',
            projects: [],
            page: 0,
            size: 10,
            totalElements: 0,
            totalPages: 0,
            last: true,
            isLoading: false
        };
    }

    loadProjectList = (page = 0, size = PROJECT_LIST_SIZE) => {
        let promise = getAllProjects(page, size);
        
        if(this.props.username) {
            if(this.props.type === 'USER_CREATED_PROJECTS') {
                promise = getUserPlacedBids(this.props.username, page, size);
            } 
        } else {
            promise = getAllProjects(page, size);
        }

        if(!promise) {
            return;
        }

        this.setState({
            isLoading: true
        });

        promise            
        .then(response => {
            const projects = this.state.projects.slice();
            
            this.setState({
                projects: projects.concat(response.content),
                page: response.page,
                size: response.size,
                totalElements: response.totalElements,
                totalPages: response.totalPages,
                last: response.last,
                isLoading: false
            })
        }).catch(error => {
            this.setState({
                isLoading: false
            })
        });  
        
    }

    componentWillMount() {
        this.loadProjectList();
    }

    componentWillReceiveProps(nextProps) {
        if(this.props.isAuthenticated !== nextProps.isAuthenticated) {
            // Reset State
            this.setState({
                projects: [],
                page: 0,
                size: 10,
                totalElements: 0,
                totalPages: 0,
                last: true,
                isLoading: false
            });    
            this.loadProjectList();
        }
    }

    render() {
        return (
        <div className="project-grid">
            <Grid
            rows={
                this.state.projects
            }
            columns={[
                { name: "id", title: "ID" },
                { name: "name", title: "Project Name" },
                { name: "budget", title: "Budget" },
                { name: "bidExpiry", title: "Bid Deadline" }
            ]}
            >
            <Table />
            <TableHeaderRow />
            </Grid>
        </div>
        );
    }
}

export default withRouter(ProjectList);
