import React, {Component} from "react";
import {getProjectById, placeBid} from "util/APIUtils";
import LoadingIndicator from "common/LoadingIndicator";
import {Button, Form, InputNumber, notification} from "antd";
import {withRouter} from "react-router-dom";
import "./ProjectSearch.css";

const FormItem = Form.Item;

class ProjectSearch extends Component {
    loadProjectInfo = () => {
        let promise = getProjectById(this.state.query);

        if (!promise) {
            return;
        }

        this.setState({
            isLoading: true
        });

        promise
            .then(response => {
                this.setState({
                    project: response,
                    isLoading: false,
                    pageError: ""
                })
            }).catch(error => {
            this.setState({
                isLoading: false
            })
            if (error.status === 400) {
                let errorTitle = 'Enter a valid numeric project Id';
                this.setState({
                    pageError: errorTitle,
                    isLoading: false
                });
            }
            else if (error.status === 401) {
                this.props.handleLogout('/login', 'error', 'You have been logged out. Please login create project.');
            } else {
                let errorTitle = 'Project does not exist!';
                this.setState({
                    pageError: errorTitle,
                    isLoading: false
                });
            }
        });
    };

    constructor(props) {
        super(props);
        this.state = {
            query: '',
            project: '',
            isLoading: true,
            bid: '',
            placedBid: '',
            validateStatus: '',
            pageError: ''
        };
    }

    componentWillMount() {
        if (this.props.location.query) {
            this.setState({
                query: this.props.location.query,
                isLoading: true
            }, () => {
                this.loadProjectInfo(this.state)
            });
        }
    }

    validatePlacedBid = (value) => {
        if (value === null || value === "") {
            return {
                validateStatus: null,
                errorMsg: null
            }
        }
        else if (value.length === 0) {
            return {
                validateStatus: 'error',
                errorMsg: 'Please enter a valid bid value'
            }
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null
            }
        }
    }

    componentWillReceiveProps(nextProps) {
        this.setState({
            query: nextProps.location.query,
            isLoading: true
        }, () => {
            this.loadProjectInfo(this.state)
        });
    }

    handlePlacedBidChange = (value) => {
        if (value === null) {
            return {
                validateStatus: null,
                errorMsg: null
            }
        }
        this.setState({
            placedBid: value,
            ...this.validatePlacedBid(value)
        });
    }

    isFormInvalid = () => {
        if (this.state.validateStatus !== 'success') {
            return true;
        }
        return false;
    }

    handleSubmit = (event) => {
        event.preventDefault();
        const bidInfo = {
            projectId: this.state.project.id,
            bid: this.state.placedBid
        };

        placeBid(bidInfo)
            .then(response => {
                this.props.history.push("/");
            }).catch(error => {
            if (error.status === 401) {
                this.props.handleLogout('/login', 'error', 'You have been logged out. Please login create project.');
            }else{
                notification.error({
                    message: 'Job Marketplace',
                    description: error.message
                });
            }
        });
    }

    render() {
        if (!this.state.query) {
            return (
                <div className="page-not-found">
                    <div className="desc">
                        Enter Project Id in the Search Field
                    </div>
                </div>
            );
        } else if (this.state.isLoading) {
            return <LoadingIndicator/>;
        } else if (this.state.pageError) {
            return (
                <div className="page-not-found">
                    <div className="desc">
                        {this.state.pageError}
                    </div>
                </div>
            );
        } else {
            return (
                <div className="project-container">
                    <div className="project-box">
                        <div className="project-prop-name"> Name:</div>
                        <div> {this.state.project.name} </div>
                        <div className="project-prop-name"> Description:</div>
                        <textarea style={{border: "dotted 1px"}} rows="4" cols="80"
                                  maxLength="400" readOnly value={this.state.project.description}></textarea>
                        <div className="project-prop-name"> Budget:</div>
                        <div> {this.state.project.budget} <span> USD </span></div>
                        <div className="project-prop-name"> Bid Expiration:</div>
                        <div> {this.state.project.bidExpiry} </div>
                        <div className="project-prop-name"> Your Bid:</div>
                        <Form onSubmit={this.handleSubmit} className="place-bid-form">
                            <FormItem validateStatus={this.state.validateStatus}>
                                <InputNumber
                                    min={1}
                                    step={0.1}
                                    className="bid-input"
                                    onChange={this.handlePlacedBidChange}
                                />
                            </FormItem>
                            <span> USD </span>
                            <FormItem>
                                <Button type="primary"
                                        htmlType="submit"
                                        size="large"
                                        disabled={this.isFormInvalid()}
                                        className="bid-submit-button">Place Bid</Button>
                            </FormItem>
                        </Form>
                    </div>
                </div>
            );
        }
    }
}

export default withRouter(ProjectSearch);
