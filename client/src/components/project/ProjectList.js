import React, {Component} from "react";
import "./ProjectList.css";
import {getProjectsRowModel} from 'util/ModelUtils'
import {getAllProjects, getBidsWonBy, getUserPlacedBids} from "util/APIUtils";
import {PROJECT_LIST_SIZE} from "constants/AppConstants";
import {withRouter} from "react-router-dom";
import {Table} from 'antd';

class ProjectList extends Component {
    constructor(props) {
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

        if (this.props.username) {
            if (this.props.type === 'USER_CREATED_PROJECTS') {
                promise = getUserPlacedBids(this.props.username, page, size);
            } else if (this.props.type === 'BIDS_WON_BY_USER') {
                promise = getBidsWonBy(this.props.username, page, size);
            }
        } else {
            promise = getAllProjects(page, size);
        }

        if (!promise) {
            return;
        }

        this.setState({
            isLoading: true
        });

        promise
            .then(response => {
                this.setState({
                    projects: getProjectsRowModel(response),
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

    };

    componentWillMount() {
        this.loadProjectList();
    }

    componentWillReceiveProps(nextProps) {
        if (this.props.isAuthenticated !== nextProps.isAuthenticated) {
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
                <Table
                    dataSource={this.state.projects}
                    columns={[
                        {
                            key: "id",
                            dataIndex: "id",
                            title: "ID",
                            width: 100,
                            defaultSortOrder: 'ascend',
                            sorter: (a, b) => a.id - b.id
                        },
                        {
                            key: "name",
                            dataIndex: "name",
                            title: "Project Name",
                            width: 360,
                            sorter: (a, b) => a.id - b.id
                        },
                        {
                            key: "budget",
                            dataIndex: "budget",
                            title: "Budget (in USD)",
                            width: 180,
                            defaultSortOrder: 'ascend',
                            sorter: (a, b) => a.budget - b.budget
                        },
                        {
                            key: "bidExpiry",
                            dataIndex: "bidExpiry",
                            title: "Bid Deadline",
                            width: 130,
                            defaultSortOrder: 'ascend',
                            sorter: (a, b) => a.bidExpiry - b.bidExpiry
                        },
                    ]}/>
            </div>
        );
    }
}

export default withRouter(ProjectList);
