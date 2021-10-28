import sum, {forEach, testSec} from "./first";

test('adds 1 + 2 to equal 3', () => {
	expect(sum(1, 2)).toBe(3);
});

test('multiply 5 * 7to equal 35', () => {
	expect(testSec(5, 7)).toBe(35);
});

test("test mock", () => {
	const mockCallback = jest.fn(x => 42 + x);
	forEach([0, 1], mockCallback);

// 此 mock 函数被调用了两次
	expect(mockCallback.mock.calls.length).toBe(2);

// 第一次调用函数时的第一个参数是 0
	expect(mockCallback.mock.calls[0][0]).toBe(0);

// 第二次调用函数时的第一个参数是 1
	expect(mockCallback.mock.calls[1][0]).toBe(1);

// 第一次函数调用的返回值是 42
	expect(mockCallback.mock.results[0].value).toBe(42);
});
