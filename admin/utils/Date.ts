const formatDate = (date: string | null | undefined): string => {
  if (!date) return "";
  const d = new Date(date);
  const year = d.getUTCFullYear();
  const month = String(d.getUTCMonth() + 1).padStart(2, "0"); // Tháng bắt đầu từ 0, nên cần +1
  const day = String(d.getUTCDate()).padStart(2, "0");
  return `${year}-${month}-${day}`;
};
export default formatDate;
