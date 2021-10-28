function sum(a, b) {
	return a + b;
}

const testSec= (a,b) =>{
	return a* b;
}
function forEach(items, callback) {
	for (let index = 0; index < items.length; index++) {
		callback(items[index]);
	}
}

export default sum;

export {testSec,forEach}