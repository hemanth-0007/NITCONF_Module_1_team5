import "./index.css";
import { useParams } from "react-router-dom";
import { BsDashSquare,BsPlusSquare } from "react-icons/bs";

const PaperDetailedView = (props) => {
  // get the id from the url path params
  // const paperId = props.match.params.id;
  const { paperId } = useParams();
  console.log(paperId);

  const title = "Sample Title";
  const description =
    "This is a sample description. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam convallis purus nec turpis facilisis, nec vestibulum justo luctus.";
  const deadline = "January 31, 2024";
  const numReviewers = 3;
  const version = 1.1;
  const comments = [
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
    "Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas.",
    "Donec ac lectus in libero pharetra fermentum.",
  ];
  const tags = ["tag1", "tag2", "tag3"];

  return (
    <div className="component-container">
      <h1 className="component-title">{title}</h1>
      <h2>Description</h2>
      <p className="component-description">{description}</p>
      <p className="component-deadline">
        {" "}
        <span className="font-weight-bold">Deadline</span>: {deadline}
      </p>
      <p className="component-version">
        <span className="font-weight-bold">Version :{" "}</span>
        {version}
      </p>

      <p className="component-reviewers">
        <span className="font-weight-bold">
          Number of Reviewers(Reviewed) 
        </span>
        : {numReviewers} out of 3
      </p>
      <span className="component-tags-heading font-weight-bold">Tags</span>
      <ul className="tags-list">
        {tags.map((tag, index) => (
          <li key={index} className="component-tag">
            {tag}
          </li>
        ))}
      </ul>
      <div className="component-comments">
        <h2>Comments:</h2>
        <ul className="component-comment-item">
          {comments.map((comment, index) => (
            <li key={index}>{comment}</li>
          ))}
        </ul>
      </div>
      <button
        type="button"
        className="component-button btn btn-primary cursor-pointer font-weight-bold"
      >
        Modify Paper{" "}
      </button>
    </div>
  );
};

export default PaperDetailedView;
