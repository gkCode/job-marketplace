import React, {Component} from "react";
import "./ProjectSearch.css";
import {withRouter} from "react-router-dom";
import {BUDGET_REGEX} from 'constants/AppConstants'
import {getProjectById, placeBid} from "util/APIUtils";
import LoadingIndicator from "common/LoadingIndicator";
import {Button, Form, InputNumber, message} from "antd";
import Moment from "react-moment";

const FormItem = Form.Item;

class ProjectSearch extends Component {
    handlePlacedBidChange = (value) => {
        this.setState({
            placedbid: {
                value: value,
            }
        });

        if (!BUDGET_REGEX.test(value)) {
            this.setState({
                validateStatus: 'error',
                errorMsg: 'Bid is not valid'
            });
        } else {
            this.setState({
                validateStatus: 'success',
                errorMsg: null
            });
        }
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

    componentWillReceiveProps(nextProps) {
        this.setState({
            query: nextProps.location.query,
            isLoading: true
        }, () => {
            this.loadProjectInfo(this.state)
        });
    }

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
    isFormInvalid = () => {
        return this.state.validateStatus !== 'success';
    }
    handleInputChange = (event, validate) => {
        const target = event.target;
        const inputName = target.name;
        const inputValue = target.value;

        this.setState({
            [inputName]: {
                value: inputValue,
                ...validate(inputValue)
            }
        });
    }
    handleSubmit = (event) => {
        event.preventDefault();
        const bidInfo = {
            projectId: this.state.project.id,
            bid: this.state.placedBid.value
        };

        placeBid(bidInfo)
            .then(response => {
                this.props.history.push("/");
            }).catch(error => {
            if (error.status === 401) {
                this.props.handleLogout('/login', 'error', 'You have been logged out. Please login create project.');
            } else {
                message.error(error.message);
            }
        });
    }

    constructor(props) {
        super(props);
        this.state = {
            query: '',
            project: '',
            isLoading: true,
            currentBid: '',
            placedBid: {
                value: ''
            },
            errorMsg: '',
            validateStatus: '',
            pageError: ''
        };
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
                        <textarea style={{border: "dotted 1px"}} rows="5" cols="10"
                                  maxLength="400" readOnly value={this.state.project.description}></textarea>
                        <div className="project-prop-name"> Budget:</div>
                        <div> {this.state.project.budget} </div>
                        <div className="project-prop-name"> Bid Expiration:</div>
                        <div><Moment>{this.state.project.bidExpiry}</Moment></div>
                        <div className="project-prop-name"> Your Bid:</div>
                        <Form onSubmit={this.handleSubmit} className="place-bid-form">
                            <FormItem
                                validateStatus={this.state.validateStatus}
                                help={this.state.errorMsg}>
                                <InputNumber
                                    min={1}
                                    step={0.1}
                                    className="bid-input"
                                    onChange={this.handlePlacedBidChange}
                                />
                            </FormItem>
                            <div className="currency">USD</div>
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
