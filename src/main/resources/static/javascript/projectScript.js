function lasciaCommento(task_id){
	document.getElementById("leaveCommentForm"+task_id).hidden = false;
}
function nascondiFormCommento(task_id){
	document.getElementById("leaveCommentForm"+task_id).hidden = true;

}


function showComments(task_id){
	document.getElementById("commenti"+task_id).hidden = false;
	document.getElementById("bottoneShow"+task_id).hidden = true;
	document.getElementById("bottoneHide"+task_id).hidden = false;
}
function hideComments(task_id){
	document.getElementById("commenti"+task_id).hidden = true;
	document.getElementById("bottoneShow"+task_id).hidden = false;
	document.getElementById("bottoneHide"+task_id).hidden = true;
}