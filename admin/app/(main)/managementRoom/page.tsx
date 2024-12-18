import dynamic from "next/dynamic";

const IssueTracker = dynamic(() => import("./component/IssueTracker"), {
  ssr: false,
});

export default function Page() {
  return <IssueTracker />;
}
