export function getProjectsRowModel(projects) {
    console.log(projects);
    let i = 0;
    return projects.content.map((obj) => {
        obj.key = (++i) + "";
        return obj;
    })
}

export function getProjectsColumnModel(projects) {
    return [
        {key: "id", dataIndex: "id", title: "ID"},
        {key: "name", dataIndex: "id", title: "Project Name"},
        {key: "budget", dataIndex: "id", title: "Budget"},
        {key: "bidExpiry", dataIndex: "id", title: "Bid Deadline"},
        {key: "bid", dataIndex: "id", title: "Bid"}
    ]
}